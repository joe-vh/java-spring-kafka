package com.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import project.Application;
import project.models.Condition;
import project.services.ConditionService;
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
//@WebMvcTest(ConditionController.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class ConditionControllerTest {

	private static final String API_CONDITIONS = "/api/conditions";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private ConditionService conditionService;

	@Test
	public void shouldBeUnauthorized() throws Exception {
		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_CONDITIONS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isUnauthorized())
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindNoConditions() throws Exception {
		given(conditionService.getConditions()).willReturn(Arrays.asList());

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_CONDITIONS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindConditions() throws Exception {
		Condition condition1 = new Condition();
		condition1.setId(1L);
		condition1.setField("delta");
		condition1.setCondition("==");
		condition1.setValue(new BigDecimal(1));
		condition1.setOccurred(false);

		Condition condition2 = new Condition();
		condition2.setId(2L);
		condition2.setField("vega");
		condition2.setCondition("<");
		condition2.setValue(new BigDecimal(2));
		condition2.setOccurred(false);

		given(conditionService.getConditions()).willReturn(Arrays.asList(condition1, condition2));

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_CONDITIONS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].field").value("delta"))
				.andExpect(jsonPath("$[0].condition").value("=="))
				.andExpect(jsonPath("$[0].value").value(1))
				.andExpect(jsonPath("$[0].occurred").value(false))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].field").value("vega"))
				.andExpect(jsonPath("$[1].condition").value("<"))
				.andExpect(jsonPath("$[1].value").value(2))
				.andExpect(jsonPath("$[1].occurred").value(false))
		;
	}

//
}
