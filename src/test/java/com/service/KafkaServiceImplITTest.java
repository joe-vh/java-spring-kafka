package com.service;

import project.Application;
import project.models.Basket;
import project.models.Future;
import project.models.Position;
import project.models.Strategy;
import project.payload.request.ConditionTemplate;
import project.repositories.BasketRepository;
import project.repositories.FutureRepository;
import project.repositories.PositionRepository;
import project.repositories.StrategyRepository;
import project.services.StrategyService;
import project.services.KafkaServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import javax.script.ScriptException;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.util.Arrays;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class KafkaServiceImplITTest {

	@Autowired
	private KafkaServiceImpl kafkaService;

	@Autowired
	private StrategyService strategyService;

	@Autowired
	private FutureRepository futureRepository;

	@Autowired
	private StrategyRepository strategyRepository;

	@Autowired
	private PositionRepository positionRepository;

	@Autowired
	private BasketRepository basketRepository;

	private static Future createFuture() {
		Future future = new Future();
		future.setId(1L);
		future.setName("test");

		return future;
	}

	private static Basket createBasket() {
		Basket basket = new Basket();
		basket.setId(1L);

		return basket;
	}

	private static Position createPosition(Basket basket, Future future) {
		Position position = new Position();
		position.setId(1L);
		position.setBasket(basket);
		position.setQuantity(4);
		position.setFuture(future);

		return position;
	}

	private Strategy createStrategy(Basket basket) {
		ConditionTemplate conditionTemplate  = new ConditionTemplate();
		conditionTemplate.setField("delta");
		conditionTemplate.setCondition("==");
		conditionTemplate.setValue(new BigDecimal(1));

		strategyService.addStrategy(
				"test",
				1l,
				4,
				null,
				true,
				1l,
				null,
				null,
				Arrays.asList(conditionTemplate)
		);

		return strategyService.getStrategy(1l);
	}

	@Test
	public void shouldOpenPositions() throws Exception, IOException, ParseException, ScriptException {
		Future future = createFuture();
		futureRepository.save(future);

		Basket basket = createBasket();
		basketRepository.save(basket);

		Position position = createPosition(basket, future);
		positionRepository.save(position);

		Strategy strategy = createStrategy(basket);
		strategyRepository.save(strategy);

		kafkaService.consume("{\"type\": \"C\", \"vol\": \"2\", \"timestamp\": \"2020-09-14T15:14:29.728Z\", \"price\": \"2\", \"vega\": \"2\", \"futureId\": 1, \"maturity\": \"2020-12-19T23:00:00.000Z\", \"futurePrice\": \"2\", \"t\": \"2\", \"rho\": \"2\", \"delta\": \"2\", \"strike\": \"2\", \"theta\": \"2\", \"r\": \"2\", \"gamma\": \"2\"}");

		Iterable<Position> resultantPositions = positionRepository.findByUserIdAndBasketIdAndFutureId(1l,1l, 1l);

		assertEquals(1l, resultantPositions.iterator().next().getId().longValue());
		assertEquals(0l, resultantPositions.iterator().next().getQuantity().longValue());
	}
}
