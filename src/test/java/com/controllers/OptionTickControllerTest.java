package com.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
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
import project.Application;
import project.models.Option;
import project.models.OptionTick;
import project.services.OptionTickService;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest(OptionTickController.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class OptionTickControllerTest {

	private static final String API_OPTION_TICKS = "/api/option-ticks";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private OptionTickService optionTickService;

	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	@Test
	public void shouldBeUnauthorized() throws Exception {
		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_OPTION_TICKS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isUnauthorized())
		;
	}


	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindOptionTick() throws Exception {
		Option option = new Option();
		option.setId(1l);
		option.setName("a");

		OptionTick optionTick = new OptionTick();
		optionTick.setId(1l);
		optionTick.setType("C");
		optionTick.setOption(option);
		optionTick.setMaturity(df.parse("2020-12-19T23:00:00.000Z"));
		optionTick.setStrike(new BigDecimal(2l));
		optionTick.setVol(new BigDecimal(2l));
		optionTick.setSpot(new BigDecimal(2l));
		optionTick.setPrice(new BigDecimal(2l));
		optionTick.setStrike(new BigDecimal(2l));
		optionTick.setT(new BigDecimal(2l));
		optionTick.setR(new BigDecimal(2l));
		optionTick.setGamma(new BigDecimal(2l));
		optionTick.setDelta(new BigDecimal(2l));
		optionTick.setVega(new BigDecimal(2l));
		optionTick.setRho(new BigDecimal(2l));
		optionTick.setTheta(new BigDecimal(2l));
		optionTick.setTimestamp(df.parse("2020-08-14T15:14:29.728Z"));

		given(optionTickService.getOptionTick(1l)).willReturn(optionTick);

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_OPTION_TICKS + "/" + 1)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(
						"{\"type\": \"C\", \"vol\": \"2\", \"timestamp\": \"2020-09-14T15:14:29.728Z\", \"price\": \"2\", \"vega\": \"2\", \"optionId\": 1, \"maturity\": \"2020-12-19T23:00:00.000Z\", \"optionPrice\": \"2\", \"t\": \"2\", \"rho\": \"2\", \"delta\": \"2\", \"strike\": \"2\", \"theta\": \"2\", \"r\": \"2\", \"gamma\": \"2\"}"
				))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindNoOptionTicks() throws Exception {
		given(optionTickService.getOptionTicks(1l)).willReturn(Arrays.asList());

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_OPTION_TICKS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindOptionTicks() throws Exception {
		OptionTick optionTick1 = new OptionTick();
		optionTick1.setId(1L);
		optionTick1.setType("C");
		optionTick1.setSpot(new BigDecimal(40l));

		OptionTick optionTick2 = new OptionTick();
		optionTick2.setId(2L);
		optionTick2.setType("P");
		optionTick2.setSpot(new BigDecimal(60l));

		given(optionTickService.getOptionTicks(1l)).willReturn(Arrays.asList(optionTick1, optionTick2));

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_OPTION_TICKS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].type").value("C"))
				.andExpect(jsonPath("$[0].spot").value(40))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].type").value("P"))
				.andExpect(jsonPath("$[1].spot").value(60))
		;
	}

}
