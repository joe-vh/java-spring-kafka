package com.controllers;

import project.Application;
import project.services.PositionService;
import project.models.Position;
import project.payload.request.PositionRequest;
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
import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@WebMvcTest(PositionController.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class PositionControllerTest {

	private static final String API_POSITIONS = "/api/positions";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private PositionService positionService;

	@Test
	public void shouldBeUnauthorized() throws Exception {
		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_POSITIONS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isUnauthorized())
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindNoPositions() throws Exception {
		given(positionService.getPositions()).willReturn(Arrays.asList());

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_POSITIONS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindPositions() throws Exception {
		Position position1 = new Position();
		position1.setId(1L);
		position1.setQuantity(4);

		Position position2 = new Position();
		position2.setId(2L);
		position2.setQuantity(4);

		given(positionService.getPositions()).willReturn(Arrays.asList(position1, position2));

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_POSITIONS)
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
	public void shouldSavePosition() throws Exception {
		PositionRequest positionRequest  = new PositionRequest();
		positionRequest.setBasketId(1l);
		positionRequest.setInstrumentId(1l);
		positionRequest.setTickId(1l);
		positionRequest.setQuantity(4);
		positionRequest.setPrice(new BigDecimal(4f));
		positionRequest.setType("future");

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.post(API_POSITIONS)
								.content(objectMapper.writeValueAsString(positionRequest))
								.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldNotSavePositionWithoutData() throws Exception {
		PositionRequest positionRequest  = new PositionRequest();

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.post(API_POSITIONS)
								.content(objectMapper.writeValueAsString(positionRequest ))
								.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldNotSavePositionWithoutQuantity() throws Exception {
		PositionRequest positionRequest  = new PositionRequest();
		positionRequest.setBasketId(1l);
		positionRequest.setInstrumentId(1l);
		positionRequest.setTickId(1l);
//		positionRequest.setQuantity(4);
		positionRequest.setPrice(new BigDecimal(4f));
		positionRequest.setType("future");

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.post(API_POSITIONS)
								.content(objectMapper.writeValueAsString(positionRequest))
								.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

//
}
