package project.services;
import project.models.*;
import project.models.*;
import project.payload.request.ConditionTemplate;
import project.repositories.*;
import project.repositories.*;
import project.security.UserDetailsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class StrategyServiceImpl implements StrategyService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StrategyRepository strategyRepository;

	@Autowired
	private BasketRepository basketRepository;

	@Autowired
	private FutureRepository futureRepository;

	@Autowired
	private OptionRepository optionRepository;

	@Autowired
	private CfdRepository cfdRepository;

	@Autowired
	EntityManagerFactory entityManagerFactory;

	@Autowired
	SimpMessagingTemplate template;

	@Autowired
	ElasticSearchService elasticSearchService;

	public List<Strategy> getStrategies() {
		Long userId = UserDetailsFactory.getId();
		return strategyRepository.findByUserIdAndOccurred(userId, false);
	}

	public Strategy getStrategy(Long id) {
		return strategyRepository.findById(id).orElse(null);
	}



	public List<Strategy> getStrategiesByBasketId(Long basketId) {
		Long userId = UserDetailsFactory.getId();
		return strategyRepository.findByUserIdAndBasketIdAndOccurred(userId, basketId, false);
	}

	public void addStrategy(
		String name,
		Long basketId,
		Integer quantity,
		Boolean side,
		Boolean open,
		Long futureId,
		Long optionId,
		Long cfdId,
		List<ConditionTemplate> conditions
	) {
		Long userId = UserDetailsFactory.getId();
		User user = userRepository.findById(userId).orElse(null);

		Basket basket = null;
		Future future = null;
		Option option = null;
		Cfd cfd = null;

		if (basketId != null) {
			basket = basketRepository.findById(basketId).orElse(null);
		}

		if (futureId != null) {
			future = futureRepository.findById(futureId).orElse(null);
		}

		if (optionId != null) {
			option = optionRepository.findById(optionId).orElse(null);
		}

		if (cfdId != null) {
			cfd = cfdRepository.findById(cfdId).orElse(null);
		}

		Strategy newStrategy = new Strategy();

		List <Condition> newConditions = conditions.stream().map((ConditionTemplate c) -> {
			Condition newCondition = new Condition();
			newCondition.setUser(user);
			newCondition.setStrategy(newStrategy);
			newCondition.setField(c.getField());
			newCondition.setBackwardation(c.getBackwardation());

			if (c.getFutureId() != null) {
				newCondition.setFuture(futureRepository.findById(c.getFutureId()).orElse(null));
			}

			if (c.getOptionId() != null) {
				newCondition.setOption(optionRepository.findById(c.getOptionId()).orElse(null));
			}

			if (c.getCfdId() != null) {
				newCondition.setCfd(cfdRepository.findById(c.getCfdId()).orElse(null));
			}

			newCondition.setValue(c.getValue());
			newCondition.setCondition(c.getCondition());
			newCondition.setOccurred(false);
			return newCondition;
		}).collect(Collectors.toList());

		newStrategy.setUser(user);
		newStrategy.setName(name);
		newStrategy.setBasket(basket);
		newStrategy.setQuantity(quantity);
		newStrategy.setSide(side);
		newStrategy.setOccurred(false);
		newStrategy.setOpen(open);
		newStrategy.setFuture(future);
		newStrategy.setOption(option);
		newStrategy.setCfd(cfd);
		newStrategy.setConditions(newConditions);

		strategyRepository.save(newStrategy);

//		elasticSearchService.updateRecord(userId);

		template.convertAndSend("/topic/reload/" + userId, "Strategy added");
	}

	public void deleteStrategy(Long id) {
		Long userId = UserDetailsFactory.getId();
		Strategy strategy = strategyRepository.findByIdAndUserId(id, userId).orElse(null);
		strategyRepository.delete(strategy);
		template.convertAndSend("/topic/reload/" + userId, "Strategy deleted");
	}

}
