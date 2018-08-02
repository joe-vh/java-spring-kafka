package com.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import project.Application;
import project.models.Signal;
import project.payload.request.SignalRequest;
import project.services.SignalService;
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
//@WebMvcTest(SignalController.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class SignalControllerTest {

	private static final String API_SIGNALS = "/api/signals";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private SignalService signalService;

	@Test
	public void shouldBeUnauthorized() throws Exception {
		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_SIGNALS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isUnauthorized())
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindNoSignals() throws Exception {
		given(signalService.getSignals()).willReturn(Arrays.asList());

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_SIGNALS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindSignals() throws Exception {
		Signal signal1 = new Signal();
		signal1.setId(1L);
		signal1.setName("a");
		signal1.setUrl("abc.com");

		Signal signal2 = new Signal();
		signal2.setId(2L);
		signal2.setName("b");
		signal2.setUrl("efg.com");

		given(signalService.getSignals()).willReturn(Arrays.asList(signal1, signal2));

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_SIGNALS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].name").value("a"))
				.andExpect(jsonPath("$[0].url").value("abc.com"))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].name").value("b"))
				.andExpect(jsonPath("$[1].url").value("efg.com"))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldSaveSignal() throws Exception {
		SignalRequest signalRequest  = new SignalRequest();
		signalRequest.setName("a");
		signalRequest.setUrl("abc.com");

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.post(API_SIGNALS)
								.content(objectMapper.writeValueAsString(signalRequest))
								.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldNotSaveSignalWithoutData() throws Exception {
		SignalRequest signalRequest  = new SignalRequest();

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.post(API_SIGNALS)
								.content(objectMapper.writeValueAsString(signalRequest ))
								.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldNotSaveSignalWithoutName() throws Exception {
		SignalRequest signalRequest  = new SignalRequest();
		signalRequest.setUrl("abc.com");

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.post(API_SIGNALS)
								.content(objectMapper.writeValueAsString(signalRequest))
								.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldNotSaveSignalWithoutUrl() throws Exception {
		SignalRequest signalRequest  = new SignalRequest();
		signalRequest.setName("a");

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.post(API_SIGNALS)
								.content(objectMapper.writeValueAsString(signalRequest))
								.contentType(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest());
	}

//
}
