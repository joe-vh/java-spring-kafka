package com.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import project.Application;
import project.models.Strategy;
import project.payload.request.ConditionTemplate;
import project.payload.request.StrategyRequest;
import project.services.StrategyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@WebMvcTest(StrategyController.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class StrategyControllerTest {

	private static final String API_STRATEGIES = "/api/strategies";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private StrategyService strategyService;

	@Test
	public void shouldBeUnauthorized() throws Exception {
		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_STRATEGIES)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isUnauthorized())
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindNoStrategies() throws Exception {
		given(strategyService.getStrategies()).willReturn(Arrays.asList());

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_STRATEGIES)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindStrategies() throws Exception {
		Strategy strategy1 = new Strategy();
		strategy1.setId(1L);
		strategy1.setQuantity(4);

		Strategy strategy2 = new Strategy();
		strategy2.setId(2L);
		strategy2.setQuantity(4);

		given(strategyService.getStrategies()).willReturn(Arrays.asList(strategy1, strategy2));

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_STRATEGIES)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].quantity").value(4))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].quantity").value(4))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldSaveStrategy() throws Exception {
		StrategyRequest strategyRequest  = new StrategyRequest();
		strategyRequest.setBasketId(1l);
		strategyRequest.setName("a");
		strategyRequest.setSide(true);
		strategyRequest.setOpen(true);
		strategyRequest.setQuantity(4);

		ConditionTemplate conditionTemplate  = new ConditionTemplate();
		conditionTemplate.setField("delta");
		conditionTemplate.setCondition("==");
		conditionTemplate.setValue(new BigDecimal(1));

		strategyRequest.setConditions(Arrays.asList(conditionTemplate));

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.post(API_STRATEGIES)
								.content(objectMapper.writeValueAsString(strategyRequest))
								.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldNotSaveStrategyWithoutData() throws Exception {
		StrategyRequest strategyRequest  = new StrategyRequest();

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.post(API_STRATEGIES)
								.content(objectMapper.writeValueAsString(strategyRequest ))
								.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldNotSaveStrategyWithoutConditions() throws Exception {
		StrategyRequest strategyRequest  = new StrategyRequest();
		strategyRequest.setBasketId(1l);
		strategyRequest.setName("a");
		strategyRequest.setSide(true);
		strategyRequest.setOpen(true);
		strategyRequest.setQuantity(4);

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.post(API_STRATEGIES)
								.content(objectMapper.writeValueAsString(strategyRequest))
								.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldNotSaveStrategyWithoutValidConditionType() throws Exception {
		StrategyRequest strategyRequest  = new StrategyRequest();
		strategyRequest.setBasketId(1l);
		strategyRequest.setName("a");
		strategyRequest.setSide(true);
		strategyRequest.setOpen(true);
		strategyRequest.setQuantity(4);

		ConditionTemplate conditionTemplate  = new ConditionTemplate();
		conditionTemplate.setField("delta");
		conditionTemplate.setCondition("invalid");
		conditionTemplate.setValue(new BigDecimal(1));

		strategyRequest.setConditions(Arrays.asList(conditionTemplate));

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.post(API_STRATEGIES)
								.content(objectMapper.writeValueAsString(strategyRequest))
								.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

//
}
