package com.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import project.Application;
import project.models.User;
import project.services.UserService;
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
//@WebMvcTest(UserController.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class UserControllerTest {

	private static final String API_USERS = "/api/users";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	@Test
	public void shouldBeUnauthorized() throws Exception {
		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_USERS + "?username=fr")
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isUnauthorized())
		;
	}



	@Test
	public void shouldBeBadRequest() throws Exception {
		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_USERS)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isBadRequest())
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindNoUsers() throws Exception {
		given(userService.getAllUsersAndStrategiesByUsername("")).willReturn(Arrays.asList());

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_USERS + "?username=")
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(0)))
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindUsers() throws Exception {
		User user1 = new User();
		user1.setId(1L);
		user1.setUsername("Fred");

		User user2 = new User();
		user2.setId(2L);
		user2.setUsername("Frank");;

		given(userService.getAllUsersAndStrategiesByUsername("fr")).willReturn(Arrays.asList(user1, user2));

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_USERS + "?username=fr")
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$", hasSize(2)))
				.andExpect(jsonPath("$[0].id").value(1))
				.andExpect(jsonPath("$[0].username").value("Fred"))
				.andExpect(jsonPath("$[1].id").value(2))
				.andExpect(jsonPath("$[1].username").value("Frank"))
		;
	}

//
}
