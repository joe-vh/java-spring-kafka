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
import project.models.Cfd;
import project.models.CfdTick;
import project.services.CfdTickService;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@RunWith(SpringRunner.class)
//@WebMvcTest(CfdTickController.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class CfdTickControllerTest {

	private static final String API_CFD_TICKS = "/api/cfd-ticks";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private CfdTickService cfdTickService;

	DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	@Test
	public void shouldBeUnauthorized() throws Exception {
		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_CFD_TICKS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isUnauthorized())
		;
	}


	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindCfdTick() throws Exception {
		Cfd cfd = new Cfd();
		cfd.setId(1l);
		cfd.setName("a");

		CfdTick cfdTick = new CfdTick();
		cfdTick.setId(1l);
		cfdTick.setBuy(new BigDecimal(1l));
		cfdTick.setSell(new BigDecimal(3l));
		cfdTick.setCfd(cfd);
		cfdTick.setPrice(new BigDecimal(2l));
		cfdTick.setTimestamp(df.parse("2020-08-14T15:14:29.728Z"));

		given(cfdTickService.getCfdTick(1l)).willReturn(cfdTick);

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_CFD_TICKS + "/" + 1)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().json(
						"{\"timestamp\": \"2020-09-14T15:14:29.728Z\", \"price\": \"2\", \"sell\": \"3\", \"cfdId\": 1, \"buy\": \"2\"}"
				))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindNoCfdTicks() throws Exception {
		given(cfdTickService.getCfdTicks(1l)).willReturn(Arrays.asList());

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_CFD_TICKS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindCfdTicks() throws Exception {
		CfdTick cfdTick1 = new CfdTick();
		cfdTick1.setId(1L);
		cfdTick1.setBuy(new BigDecimal(1l));
		cfdTick1.setSell(new BigDecimal(3l));
		cfdTick1.setPrice(new BigDecimal(2l));;

		CfdTick cfdTick2 = new CfdTick();
		cfdTick2.setId(2L);
		cfdTick2.setBuy(new BigDecimal(1l));
		cfdTick2.setSell(new BigDecimal(3l));
		cfdTick2.setPrice(new BigDecimal(2l));

		given(cfdTickService.getCfdTicks(1l)).willReturn(Arrays.asList(cfdTick1, cfdTick2));

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_CFD_TICKS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].buy").value(1))
				.andExpect(jsonPath("$[0].sell").value(3))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].buy").value(1))
				.andExpect(jsonPath("$[1].sell").value(3))
		;
	}

}
