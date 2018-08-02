package project.services;

import project.exceptions.InsufficientFundsException;
import project.exceptions.PriceOutOfRangeException;
import project.enums.EInstrument;
import project.models.*;
import project.repositories.*;
import project.security.UserDetailsFactory;
import org.json.JSONException;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
public class PositionServiceImpl implements PositionService {
	private static final Logger LOG = LoggerFactory.getLogger(PositionServiceImpl.class);

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private FutureTickRepository futureTickRepository;

	@Autowired
	private OptionTickRepository optionTickRepository;

	@Autowired
	private CfdTickRepository cfdTickRepository;

	@Autowired
	UserRepository userRepository;

	@Autowired
	BasketRepository basketRepository;

	@Autowired
	FutureTickService futureTickService;

	@Autowired
	OptionTickService optionTickService;

	@Autowired
	CfdTickService cfdTickService;

	@Autowired
	SimpMessagingTemplate template;

	public Position getPosition(Long id) {
		Long userId = UserDetailsFactory.getId();
		return positionRepository.findByIdAndUserId(id, userId).orElse(null);
	}

	public void createPosition(Long userId, Long tickId, Long intrumentId, Long basketId, Integer quantity, BigDecimal price, Boolean side, String type, String kind, Date maturity) throws JSONException {
		EInstrument eInstrument = null;

		// switch type to ensure correct transaction procedure
		switch (type) {
			case "future":
				eInstrument = EInstrument.FUTURE;
				break;
			case "option":
				eInstrument = EInstrument.OPTION;
				break;
			case "cfd":
				eInstrument = EInstrument.CFD;
				break;
		}

		// FIX request here
		RestTemplate restTemplate = new RestTemplate();

		String url = "http://0.0.0.0:7777/api/request";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject request = new JSONObject();
		request.put("inst_id", intrumentId);
		request.put("quantity", quantity);
		request.put("price", price);
		request.put("side", side);
		request.put("type", kind);
		request.put("maturity", maturity);
		request.put("open", true);

		// check balance
		Account account = accountRepository.findByUserId(userId).orElse(null);

		if (account.getFunds().compareTo(new BigDecimal(quantity).multiply(price)) >= 0) {
			HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
			String answer = restTemplate.postForObject(url, entity, String.class);

			if (answer.equals("success")) {
				User user = userRepository.findById(userId).orElse(null);
				Basket basket = basketRepository.findById(basketId).orElse(null);

				Position newPosition = new Position();
				newPosition.setUser(user);
				newPosition.setBasket(basket);
				newPosition.setQuantity(quantity);
				newPosition.setSide(side);

				switch (eInstrument) {
					case FUTURE:
						FutureTick futureTick = futureTickRepository.findById(tickId).orElse(null);
						newPosition.setFutureTick(futureTick);
						newPosition.setFuture(futureTick.getFuture());
						break;

					case OPTION:
						OptionTick optionTick = optionTickRepository.findById(tickId).orElse(null);
						newPosition.setOptionTick(optionTick);
						newPosition.setOption(optionTick.getOption());
						break;

					case CFD:
						CfdTick cfdTick = cfdTickRepository.findById(tickId).orElse(null);
						newPosition.setCfdTick(cfdTick);
						newPosition.setCfd(cfdTick.getCfd());
						break;
				}

				positionRepository.save(newPosition);

				// update balance
				account.setFunds(account.getFunds().subtract(new BigDecimal(quantity).multiply(price)));
				accountRepository.save(account);
			} else {
				// throw price now out of range error
				throw new PriceOutOfRangeException();
			}
		} else {
			// throw balance exception
			throw new InsufficientFundsException();
		}
	}

	public void closePosition(Long userId, Long basketId, Integer quantity, Boolean side, EInstrument eInstrument, Long futureId, Long optionId, Long cfdId, String kind, Date maturity) throws JSONException {
		Iterable <Position> positions = new ArrayList<>();

		FutureTick futureTick = null;
		OptionTick optionTick = null;
		CfdTick cfdTick = null;

		Long instrumentId = null;
		BigDecimal price = null;

		// get latest tick price
		switch (eInstrument) {
			case FUTURE:
				positions = positionRepository.findByUserIdAndBasketIdAndFutureId(userId, basketId, futureId);
				futureTick = futureTickService.getLatestFutureTick(futureId);
				price = futureTick.getPrice();
				instrumentId = futureId;
				break;

			case OPTION:
				positions = positionRepository.findByUserIdAndBasketIdAndOptionId(userId, basketId, optionId);
				optionTick = optionTickService.getLatestOptionTick(optionId);
				price = optionTick.getPrice();
				instrumentId = optionId;
				break;

			case CFD:
				positions = positionRepository.findByUserIdAndBasketIdAndCfdIdAndSide(userId, basketId, cfdId, side);
				cfdTick = cfdTickService.getLatestCfdTick(cfdId);
				if (side && cfdTick != null) {
					price = cfdTick.getSell();
				} else {
					price = cfdTick.getBuy();
				}
				instrumentId = cfdId;
				break;
		}

		// confirm latest price via FIX service
		RestTemplate restTemplate = new RestTemplate();

		String url = "http://0.0.0.0:7777/api/request";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject request = new JSONObject();
		request.put("inst_id", instrumentId);
		request.put("quantity", quantity);
		request.put("price", price);
		request.put("side", side);
		request.put("type", kind);
		request.put("maturity", maturity);
		request.put("open", false);

		HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
		String answer = restTemplate.postForObject(url, entity, String.class);


		if (answer.equals("success")) {
			// close Integer quantity, (if max position, close next)
			Integer counter = quantity;

			Account account = accountRepository.findByUserId(userId).orElse(null);

			// iterate positions, deduct quantity
			// update amount -> quantity - counter

			for (Position position : positions) {
				Integer q = position.getQuantity();

				if (cfdTick != null) {
					if (side == true) {
						price = cfdTick.getSell();
					} else if (side == false) {
						price = cfdTick.getBuy();
					}
				}

				if (q >= counter) {
					position.setQuantity(q - counter);
					positionRepository.save(position);
					LOG.info("position quantity closed!");
					counter = 0;
				} else if (q < counter) {
					position.setQuantity(0);
					positionRepository.save(position);
					LOG.info("position quantity closed!");
					counter -= q;
				}
			}

			// update balance below
			account.setFunds(account.getFunds().add((new BigDecimal(quantity - counter)).multiply(price)));
			accountRepository.save(account);
			LOG.info("funds updated");
			template.convertAndSend("/topic/reload/" + userId, "Position closed");
		} else {
			// throw price now out of range error
			template.convertAndSend("/topic/error/" + userId, "Price now out of range");
			throw new PriceOutOfRangeException();
		}
	}


	public void deletePosition(Long id) throws JSONException {
		Long userId = UserDetailsFactory.getId();
		RestTemplate restTemplate = new RestTemplate();
		Position position = positionRepository.findById(id).orElse(null);
		// get tick
		// tick.getIns getId()
		BigDecimal price = null;
		Boolean side = null;
		Long instrumentId = null;
		Integer quantity = position.getQuantity();
		String kind = null;
		Date maturity = null;

		if (position.getCfd() != null) {
			instrumentId = position.getCfd().getId();
			side = position.getSide();
			if (side) {
				price = position.getCfdTick().getSell();
			} else {
				price = position.getCfdTick().getBuy();
			}
		}

		else if (position.getFuture() != null) {
			instrumentId = position.getFuture().getId();
			price = position.getFutureTick().getPrice();
			kind = position.getFutureTick().getType();
			maturity = position.getFutureTick().getMaturity();
		}

		else if (position.getOption() != null) {
			instrumentId = position.getOption().getId();
			price = position.getOptionTick().getPrice();
			kind = position.getOptionTick().getType();
			maturity = position.getOptionTick().getMaturity();
		}

		String url = "http://0.0.0.0:7777/api/request";
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject request = new JSONObject();
		request.put("inst_id", instrumentId);
		request.put("quantity", quantity);
		request.put("price", price);
		request.put("side", side);
		request.put("type", kind);
		request.put("maturity", maturity);
		request.put("open", false);

		HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
		String answer = restTemplate.postForObject(url, entity, String.class);


		if (answer.equals("success")) {
			// close Integer quantity, (if max position, close next)
			LOG.info("FIX success! close..");

			Account account = accountRepository.findByUserId(userId).orElse(null);
			// update amount -> quantity - counter

			// update balance below
			account.setFunds(account.getFunds().add(new BigDecimal(quantity).multiply(price)));
			accountRepository.save(account);
			LOG.info("funds updated");

			position.setQuantity(0);
			positionRepository.save(position);
			template.convertAndSend("/topic/reload/" + userId, "Position closed");
//			positionRepository.delete(position);
		} else {
			// throw price now out of range error
			template.convertAndSend("/topic/error/" + userId, "Price now out of range");
			throw new PriceOutOfRangeException();
		}
	}


	public List<Position> getPositions() {
		Long userId = UserDetailsFactory.getId();
		return positionRepository.findByUserIdAndQuantityNot(userId, 0);
	}


	public List<Position> getAllPositionsByBasket(Long basketId) {
		Long userId = UserDetailsFactory.getId();
		return positionRepository.findByUserIdAndBasketIdAndQuantityNot(userId, basketId, 0);
	}

}
