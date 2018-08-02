package com.controllers;

import com.fasterxml.jackson.databind.ObjectMapper;
import project.Application;
import project.models.Account;
import project.services.AccountService;
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

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
//@WebMvcTest(AccountController.class)
@SpringBootTest(classes = {Application.class})
@AutoConfigureMockMvc
public class AccountControllerTest {

	private static final String API_ACCOUNT = "/api/account";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private AccountService accountService;

	@Test
	public void shouldBeUnauthorized() throws Exception {
		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_ACCOUNT)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isUnauthorized())
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindNoAccount() throws Exception {
		given(accountService.getAccount()).willReturn(null);

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_ACCOUNT)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$").doesNotExist())
		;
	}

	@Test
	@WithMockUser(username = "admin", roles = { "ADMIN", "USER", "MODERATOR" })
	public void shouldFindAccount() throws Exception {
		Account account1 = new Account();
		account1.setId(1L);
		account1.setFunds(new BigDecimal(4000));

		given(accountService.getAccount()).willReturn(account1);

		mockMvc
				.perform(
						MockMvcRequestBuilders
								.get(API_ACCOUNT)
								.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(jsonPath("$.id").value(1))
				.andExpect(jsonPath("$.funds").value(4000))
//				.andExpect(content().json("{\"id\": 1, \"funds\": 4000}"))
		;
	}
}
