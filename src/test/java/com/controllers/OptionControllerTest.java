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
import project.services.OptionService;

import java.util.Arrays;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@WebMvcTest(OptionController.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class OptionControllerTest {

	private static final String API_OPTIONS = "/api/options";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private OptionService optionService;

	@Test
	public void shouldBeUnauthorized() throws Exception {
		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_OPTIONS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isUnauthorized())
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindNoOptions() throws Exception {
		given(optionService.getAllOptions()).willReturn(Arrays.asList());

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_OPTIONS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindOption() throws Exception {
		Option option = new Option();
		option.setId(1l);
		option.setName("test");

		given(optionService.getOption(1l)).willReturn(option);

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_OPTIONS + "/" + 1)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.name").value("test"))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindOptions() throws Exception {
		Option option1 = new Option();
		option1.setId(1L);
		option1.setName("a");

		Option option2 = new Option();
		option2.setId(2L);
		option2.setName("b");

		given(optionService.getAllOptions()).willReturn(Arrays.asList(option1, option2));

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_OPTIONS)
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

}
