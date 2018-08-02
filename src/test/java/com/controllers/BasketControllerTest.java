package com.controllers;

import project.Application;
import project.services.BasketService;
import com.fasterxml.jackson.databind.ObjectMapper;
import project.models.Basket;
import project.payload.request.BasketRequest;
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

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@WebMvcTest(BasketController.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class BasketControllerTest {

	private static final String API_BASKETS = "/api/baskets";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private BasketService basketService;

	@Test
	public void shouldBeUnauthorized() throws Exception {
		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_BASKETS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isUnauthorized())
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindNoBaskets() throws Exception {
		given(basketService.getBaskets()).willReturn(Arrays.asList());

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_BASKETS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindBaskets() throws Exception {
		Basket basket1 = new Basket();
		basket1.setId(1L);
		basket1.setName("a");

		Basket basket2 = new Basket();
		basket2.setId(2L);
		basket2.setName("b");

		given(basketService.getBaskets()).willReturn(Arrays.asList(basket1, basket2));

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_BASKETS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].name").value("a"))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].name").value("b"))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldSaveBasket() throws Exception {
		BasketRequest basketRequest  = new BasketRequest();
		basketRequest.setName("a");

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.post(API_BASKETS)
								.content(objectMapper.writeValueAsString(basketRequest))
								.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldNotSaveBasketWithoutData() throws Exception {
		BasketRequest basketRequest  = new BasketRequest();

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.post(API_BASKETS)
								.content(objectMapper.writeValueAsString(basketRequest ))
								.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	public void shouldNotSaveBasketWithoutName() throws Exception {
		BasketRequest basketRequest  = new BasketRequest();
		basketRequest.setName(null);

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.post(API_BASKETS)
								.content(objectMapper.writeValueAsString(basketRequest))
								.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

//
}
