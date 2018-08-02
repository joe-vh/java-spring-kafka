package com.service;

import project.Application;
import project.models.Condition;
import project.services.ConditionServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class ConditionServiceImplITTest {

	@Autowired
	private ConditionServiceImpl conditionService;

	@Test
	public void shouldFindConditions() {
		List<Condition> savedConditions = conditionService.getConditions();

		assertEquals(1, savedConditions.size());
		assertEquals(1L, savedConditions.get(0).getId().longValue());
	}
}
