package com.service;

import project.models.Strategy;
import project.payload.request.ConditionTemplate;
import project.services.StrategyServiceImpl;
import project.Application;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class StrategyServiceImplITTest {

	@Autowired
	private StrategyServiceImpl strategyService;

	@Test
	public void shouldSaveStrategy() {
		ConditionTemplate conditionTemplate  = new ConditionTemplate();
		conditionTemplate.setField("delta");
		conditionTemplate.setCondition(">");
		conditionTemplate.setValue(new BigDecimal(1));

		strategyService.addStrategy(
				"test",
				1l,
				4,
				null,
				false,
				1l,
				null,
				null,
				Arrays.asList(conditionTemplate)
		);

		Strategy savedStrategy = strategyService.getStrategy(1l);

		assertEquals(1L, savedStrategy.getId().longValue());
		assertEquals("test", savedStrategy.getName());
	}

	@Test
	public void shouldFindStrategy() {
		Strategy savedStrategy = strategyService.getStrategy(1l);

		assertEquals(1L, savedStrategy.getId().longValue());
		assertEquals("test", savedStrategy.getName());
	}

	@Test
	public void shouldFindStrategies() {
		List<Strategy> savedStrategies = strategyService.getStrategies();

		assertEquals(1, savedStrategies.size());
		assertEquals(1L, savedStrategies.get(0).getId().longValue());
	}

	@Test
	public void shouldFindStrategiesByBasketId() {
		List<Strategy> savedStrategies = strategyService.getStrategiesByBasketId(1l);

		assertEquals(1, savedStrategies.size());
		assertEquals(1L, savedStrategies.get(0).getId().longValue());
	}

	@Test
	public void shouldDeleteStrategy() {
		strategyService.deleteStrategy(1l);
		Strategy savedStrategy = strategyService.getStrategy(1l);

		assertEquals(null, savedStrategy);
	}
}
