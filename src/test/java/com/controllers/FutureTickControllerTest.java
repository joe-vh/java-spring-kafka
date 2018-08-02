package com.controllers;

import project.models.Future;
import project.models.FutureTick;
import project.services.FutureTickService;
import project.Application;
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

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest(FutureTickController.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class FutureTickControllerTest {

	private static final String API_FUTURE_TICKS = "/api/future-ticks";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private FutureTickService futureTickService;

	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	@Test
	public void shouldBeUnauthorized() throws Exception {
		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_FUTURE_TICKS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isUnauthorized())
		;
	}


	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindFutureTick() throws Exception {
		Future future = new Future();
		future.setId(1l);
		future.setName("a");

		FutureTick futureTick = new FutureTick();
		futureTick.setId(1l);
		futureTick.setType("C");
		futureTick.setFuture(future);
		futureTick.setMaturity(df.parse("2020-12-19T23:00:00.000Z"));
		futureTick.setStrike(new BigDecimal(2l));
		futureTick.setVol(new BigDecimal(2l));
		futureTick.setFuturePrice(new BigDecimal(2l));
		futureTick.setPrice(new BigDecimal(2l));
		futureTick.setStrike(new BigDecimal(2l));
		futureTick.setT(new BigDecimal(2l));
		futureTick.setR(new BigDecimal(2l));
		futureTick.setGamma(new BigDecimal(2l));
		futureTick.setDelta(new BigDecimal(2l));
		futureTick.setVega(new BigDecimal(2l));
		futureTick.setRho(new BigDecimal(2l));
		futureTick.setTheta(new BigDecimal(2l));
		futureTick.setTimestamp(df.parse("2020-08-14T15:14:29.728Z"));

		given(futureTickService.getFutureTick(1l)).willReturn(futureTick);

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_FUTURE_TICKS + "/" + 1)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(
						"{\"type\": \"C\", \"vol\": \"2\", \"timestamp\": \"2020-09-14T15:14:29.728Z\", \"price\": \"2\", \"vega\": \"2\", \"futureId\": 1, \"maturity\": \"2020-12-19T23:00:00.000Z\", \"futurePrice\": \"2\", \"t\": \"2\", \"rho\": \"2\", \"delta\": \"2\", \"strike\": \"2\", \"theta\": \"2\", \"r\": \"2\", \"gamma\": \"2\"}"
				))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindNoFutureTicks() throws Exception {
		given(futureTickService.getFutureTicks(1l)).willReturn(Arrays.asList());

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_FUTURE_TICKS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindFutureTicks() throws Exception {
		FutureTick futureTick1 = new FutureTick();
		futureTick1.setId(1L);
		futureTick1.setType("C");
		futureTick1.setFuturePrice(new BigDecimal(40l));

		FutureTick futureTick2 = new FutureTick();
		futureTick2.setId(2L);
		futureTick2.setType("P");
		futureTick2.setFuturePrice(new BigDecimal(60l));

		given(futureTickService.getFutureTicks(1l)).willReturn(Arrays.asList(futureTick1, futureTick2));

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_FUTURE_TICKS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].type").value("C"))
				.andExpect(jsonPath("$[0].futurePrice").value("C"))
				.andExpect(jsonPath("$[1].id").value(40))
				.andExpect(jsonPath("$[1].type").value("P"))
				.andExpect(jsonPath("$[1].futurePrice").value(40))
		;
	}

}
