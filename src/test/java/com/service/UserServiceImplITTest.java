package com.service;

import project.Application;
import project.models.User;
import project.repositories.UserRepository;
import project.services.UserServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class UserServiceImplITTest {

	@Autowired
	private UserServiceImpl userService;

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

	@Test
	public void shouldFindUsers() {
		User user = createUser();
		userRepository.save(user);
		List<Object> savedUsers = userService.getAllUsersAndStrategiesByUsername("abc123");

		assertEquals(1, savedUsers.size());
//		assertEquals(1L, savedUsers.get(0).id);
//		assertEquals("abc123", savedUsers.get(0).username);
	}
}
