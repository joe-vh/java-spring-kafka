package com.service;

import project.Application;
import project.models.Account;
import project.models.User;
import project.repositories.AccountRepository;
import project.repositories.UserRepository;
import project.services.AccountServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class AccountServiceImplITTest {

	@Autowired
	private AccountServiceImpl accountService;

	@Autowired
	private AccountRepository accountRepository;

	@Autowired
	private UserRepository userRepository;

	private static User createUser() {
		User user = new User();
		user.setId(1L);
		user.setPassword("abc123");
		user.setEmail("abc@123.com");
		user.setUsername("abc123");

		return user;
	}

	private static Account createAccount(User user) {
		Account account = new Account();
		account.setId(10L);
		account.setFunds(BigDecimal.valueOf(1.99));
		account.setUser(user);

		return account;
	}

	@Test
	public void shouldSaveAccount() {
		Integer initialSize = accountService.getAllAccounts().size();
		User user = createUser();
		userRepository.save(user);

		Account account = createAccount(user);
		accountRepository.save(account);

		List<Account> savedAccounts = accountService.getAllAccounts();

		assertEquals(initialSize + 1, savedAccounts.size());
		assertEquals(1L, savedAccounts.get(0).getId().longValue());
	}

	@Test
	public void shouldFindAccounts() {

		List<Account> savedAccounts = accountService.getAllAccounts();

		assertEquals(1, savedAccounts.size());
		assertEquals(1L, savedAccounts.get(0).getId().longValue());
	}
}
