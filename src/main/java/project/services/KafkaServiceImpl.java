package project.services;
import project.enums.EInstrument;
import project.repositories.AccountRepository;
import project.repositories.ConditionRepository;
import project.repositories.StrategyRepository;
import project.repositories.UserRepository;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import project.models.*;
import org.json.JSONException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import project.models.*;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.TimeZone;

@Service
@Transactional
public class KafkaServiceImpl implements KafkaService {
	private static final Logger LOG = LoggerFactory.getLogger(KafkaServiceImpl.class);

	@Autowired
	SimpMessagingTemplate template;

	@Autowired
	FutureTickService futureTickService;

	@Autowired
    OptionTickService optionTickService;

	@Autowired
    CfdTickService cfdTickService;

	@Autowired
	PositionService positionService;

	@Autowired
	UserRepository userRepository;

	@Autowired
	ConditionRepository conditionRepository;

	@Autowired
	StrategyRepository strategyRepository;

	@Autowired
	AccountRepository accountRepository;

	ScriptEngineManager factory = new ScriptEngineManager();
	ScriptEngine engine = factory.getEngineByName("JavaScript");

	@KafkaListener(topics="${kafka.topic}")
	public void consume(@Payload String message) throws Exception, IOException, ParseException, ScriptException {
		DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");
		df.setTimeZone(TimeZone.getTimeZone("UTC"));

		ObjectMapper mapper = new ObjectMapper();
		JsonNode json = mapper.readTree(message);

		String type = json.get("type").textValue();
		Date timestamp = df.parse(json.get("timestamp").textValue());

		ObjectNode result = json.deepCopy();

		if (type.equals("future")) {
			// save tick to db
			FutureTick futureTick = futureTickService.addFutureTick(
					json.get("kind").textValue(),
					json.get("futureId").longValue(),
					df.parse(json.get("maturity").textValue()),
					new BigDecimal(json.get("t").textValue()),
					timestamp,
					new BigDecimal(json.get("futurePrice").textValue()),
					new BigDecimal(json.get("strike").textValue()),
					new BigDecimal(json.get("r").textValue()),
					new BigDecimal(json.get("vol").textValue()),
					new BigDecimal(json.get("price").textValue()),
					new BigDecimal(json.get("delta").textValue()),
					new BigDecimal(json.get("gamma").textValue()),
					new BigDecimal(json.get("vega").textValue()),
					new BigDecimal(json.get("theta").textValue()),
					new BigDecimal(json.get("rho").textValue())
			);

			result.put("id", futureTick.getId());

			// queue for condition/strategy check
			futureThread(futureTick, json);
		}

		if (type.equals("option")) {
			// save tick to db
			OptionTick optionTick = optionTickService.addOptionTick(
					json.get("kind").textValue(),
					json.get("optionId").longValue(),
					df.parse(json.get("maturity").textValue()),
					new BigDecimal(json.get("t").textValue()),
					timestamp,
					new BigDecimal(json.get("spot").textValue()),
					new BigDecimal(json.get("strike").textValue()),
					new BigDecimal(json.get("r").textValue()),
					new BigDecimal(json.get("vol").textValue()),
					new BigDecimal(json.get("price").textValue()),
					new BigDecimal(json.get("delta").textValue()),
					new BigDecimal(json.get("gamma").textValue()),
					new BigDecimal(json.get("vega").textValue()),
					new BigDecimal(json.get("theta").textValue()),
					new BigDecimal(json.get("rho").textValue())
			);

			result.put("id", optionTick.getId());

			// queue for condition/strategy check
			optionThread(optionTick, json);
		}

		if (type.equals("cfd")) {
			// save tick to db
			CfdTick cfdTick = cfdTickService.addCfdTick(
					json.get("cfdId").longValue(),
					new BigDecimal(json.get("price").textValue()),
					new BigDecimal(json.get("buy").textValue()),
					new BigDecimal(json.get("sell").textValue()),
					new BigDecimal(json.get("spread").textValue()),
					timestamp
			);

			result.put("id", cfdTick.getId());
			// queue for condition/strategy check
			cfdThread(cfdTick, json);
		}

		// send to ui clients
		template.convertAndSend("/topic/ticks", result.toString());
	}

	@Async("threadPoolTaskExecutor")
	@Transactional
	public void futureThread(FutureTick futureTick, JsonNode json) throws Exception {
		Iterable<Condition> futureConditions = conditionRepository.findByFutureIdAndOccurred(futureTick.getFuture().getId(), false);

		for (Condition futureCondition : futureConditions) {
			if (futureCondition.getStrategy().getOccurred()) {
				continue;
			}

			// check backwardation for future only, check if condition is true
			// if so, check rest of conditions and update position
			if (
					(futureCondition.getBackwardation() == true && futureTick.getFuturePrice().floatValue() > futureTick.getStrike().floatValue())
					|| (futureCondition.getBackwardation() == false && futureTick.getFuturePrice().floatValue() < futureTick.getStrike().floatValue())
					|| (Boolean) engine.eval(json.get(futureCondition.getField()) + " " + futureCondition.getCondition() + " " + futureCondition.getValue())
			) {

				futureCondition.setOccurred(true);
				conditionRepository.save(futureCondition);


				// check all strategy conditions
				Strategy strategy = futureCondition.getStrategy();
				Iterable<Condition> strategyConditions = conditionRepository.findByStrategyId(strategy.getId());

				Boolean allConditionsMet = true;

				for (Condition strategyCondition : strategyConditions) {
					if (! strategyCondition.getOccurred()) {
						allConditionsMet = false;
						break;
					}
				}

				if (!allConditionsMet) {
					// do not execute trade or close strategy
					continue;
				}

				// if all conditions met, execute and close strategy for user
				User user = futureCondition.getUser();
				Long userId = user.getId();
				Account account = user.getAccount();

				if(strategy.getOpen() && (account.getFunds().compareTo(futureTick.getPrice().multiply(new BigDecimal(strategy.getQuantity()))) > 0)) {
					// open position for user
					try {
						positionService.createPosition(userId, futureTick.getId(), futureTick.getFuture().getId(), strategy.getBasket().getId(), strategy.getQuantity(), futureTick.getPrice(), null, "future", futureTick.getType(), futureTick.getMaturity());
						strategy.setOccurred(true);
						strategyRepository.save(strategy);

						// reload client data
						template.convertAndSend("/topic/reload/" + userId, "Position opened");
					} catch (JSONException e) {
						LOG.error(e.getMessage());
						// send error to client
						template.convertAndSend("/topic/error/" + userId, e.getMessage());
					}
				} else if (!strategy.getOpen()) {
					// close position for user
					try {
						positionService.closePosition(userId, strategy.getBasket().getId(), strategy.getQuantity(), null, EInstrument.FUTURE, futureTick.getFuture().getId(), null, null, futureTick.getType(), futureTick.getMaturity());

						strategy.setOccurred(true);
						strategyRepository.save(strategy);

						// reload client data
						template.convertAndSend("/topic/reload/" + userId, "Position closed");
					} catch (JSONException e) {
						// send error to client
						template.convertAndSend("/topic/error/" + userId, e.getMessage());
					}
				}
			}
		}
	}

	@Async("threadPoolTaskExecutor")
	@Transactional
	public void optionThread(OptionTick optionTick, JsonNode json) throws Exception {
		Iterable<Condition> optionConditions = conditionRepository.findByOptionIdAndOccurred(optionTick.getOption().getId(), false);

		for (Condition optionCondition : optionConditions) {
			if (optionCondition.getStrategy().getOccurred()) {
				continue;
			}

			// check if condition is true
			// if so, check rest of conditions and update position
			if ((Boolean) engine.eval(json.get(optionCondition.getField()) + " " + optionCondition.getCondition() + " " + optionCondition.getValue())) {

				optionCondition.setOccurred(true);
				conditionRepository.save(optionCondition);


				Strategy strategy = optionCondition.getStrategy();
				Iterable<Condition> strategyConditions = conditionRepository.findByStrategyId(strategy.getId());

				Boolean allConditionsMet = true;

				for (Condition strategyCondition : strategyConditions) {
					if (!strategyCondition.getOccurred()) {
						allConditionsMet = false;
						break;
					}
				}

				if (!allConditionsMet) {
					// do not execute trade or close strategy
					continue;
				}

				// if all strategy conditions met, execute and close strategy for user
				User user = optionCondition.getUser();
				Long userId = user.getId();
				Account account = user.getAccount();

				if(strategy.getOpen() && (account.getFunds().compareTo(optionTick.getPrice().multiply(new BigDecimal(strategy.getQuantity()))) > 0)) {
					// open position for user
					try {
						positionService.createPosition(userId, optionTick.getId(), optionTick.getOption().getId(), strategy.getBasket().getId(), strategy.getQuantity(), optionTick.getPrice(), null, "option", optionTick.getType(), optionTick.getMaturity());

						strategy.setOccurred(true);
						strategyRepository.save(strategy);

						// reload client data
						template.convertAndSend("/topic/reload/" + userId, "Position opened");
					}  catch (JSONException e) {
						// send error to client
						template.convertAndSend("/topic/error/" + userId, e.getMessage());
					}
				} else if (!strategy.getOpen()) {
					// close position for user
					try {
						positionService.closePosition(userId, strategy.getBasket().getId(), strategy.getQuantity(), null, EInstrument.OPTION, null, optionTick.getOption().getId(), null, optionTick.getType(), optionTick.getMaturity());

						strategy.setOccurred(true);
						strategyRepository.save(strategy);

						// reload client data
						template.convertAndSend("/topic/reload/" + userId, "Position closed");
					}  catch (JSONException e) {
						// send error to client
						template.convertAndSend("/topic/error/" + userId, e.getMessage());
					}
				}
			}
		}
	}

	@Async("threadPoolTaskExecutor")
	@Transactional
	public void cfdThread(CfdTick cfdTick, JsonNode json) throws Exception {
		Iterable<Condition> cfdConditions = conditionRepository.findByCfdIdAndOccurred(cfdTick.getCfd().getId(), false);

		for (Condition cfdCondition : cfdConditions) {
			if (cfdCondition.getStrategy().getOccurred()) {
				continue;
			}

			// check if condition is true
			// if so, check rest of conditions and update position
			if ((Boolean) engine.eval(json.get(cfdCondition.getField()) + " " + cfdCondition.getCondition() + " " + cfdCondition.getValue())) {
				cfdCondition.setOccurred(true);
				conditionRepository.save(cfdCondition);

				Strategy strategy = cfdCondition.getStrategy();
				Iterable<Condition> strategyConditions = conditionRepository.findByStrategyId(strategy.getId());

				Boolean allConditionsMet = true;

				for (Condition strategyCondition : strategyConditions) {
					if (!strategyCondition.getOccurred()) {
						allConditionsMet = false;
						break;
					}
				}

				if (!allConditionsMet) {
					// do not execute trade or close strategy
					continue;
				}

				// if all conditions met, execute and close strategy for user
				User user = cfdCondition.getUser();
				Long userId = user.getId();
				Account account = user.getAccount();

				// get correct price for side of cfd
				BigDecimal sidePrice = cfdTick.getSell();
				if (strategy.getSide()) {
					sidePrice = cfdTick.getBuy();
				}

				if(strategy.getOpen() && (account.getFunds().compareTo((sidePrice).multiply(new BigDecimal(strategy.getQuantity()))) > 0)) {
					// open position for user
					try {
						positionService.createPosition(userId, cfdTick.getId(), cfdTick.getCfd().getId(), strategy.getBasket().getId(), strategy.getQuantity(), sidePrice, strategy.getSide(), "cfd", null, null);

						strategy.setOccurred(true);
						strategyRepository.save(strategy);
						// reload client data
						template.convertAndSend("/topic/reload/" + userId, "Position opened");
					} catch (JSONException e) {
						// send error to client
						template.convertAndSend("/topic/error/" + userId, e.getMessage());
					}
				} else if (!strategy.getOpen()) {
					// close position for user
					try {
						positionService.closePosition(userId, strategy.getBasket().getId(), strategy.getQuantity(), strategy.getSide(), EInstrument.CFD, null, null, cfdTick.getCfd().getId(), null, null);

						strategy.setOccurred(true);
						strategyRepository.save(strategy);

						// reload client data
						template.convertAndSend("/topic/reload/" + userId, "Position closed");
					}  catch (JSONException e) {
						// send error to client
						template.convertAndSend("/topic/error/" + userId, e.getMessage());
					}
				}
			}
		}
	}
}
