package com.service;

import project.Application;
import project.models.Future;
import project.repositories.FutureRepository;
import project.repositories.UserRepository;
import project.services.FutureServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class FutureServiceImplITTest {

	@Autowired
	private FutureServiceImpl futureService;

	@Autowired
	private FutureRepository futureRepository;

	@Autowired
	private UserRepository userRepository;


	private static Future createFuture() {
		Future future = new Future();
		future.setId(1L);
		future.setName("test");

		return future;
	}

	@Test
	public void shouldFindFuture() {
		Future future = createFuture();
		futureRepository.save(future);

		Future savedFuture = futureService.getFuture(1l);

		assertEquals(1L, savedFuture.getId().longValue());
		assertEquals("test", savedFuture.getName());
	}

	@Test
	public void shouldFindFutures() {
		List<Future> savedFutures = futureService.getAllFutures();

		assertEquals(1, savedFutures.size());
		assertEquals(1L, savedFutures.get(0).getId().longValue());
		assertEquals("test", savedFutures.get(0).getName());
	}
}
