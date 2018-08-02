package com.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.Application;
import project.models.Cfd;
import project.repositories.CfdRepository;
import project.repositories.UserRepository;
import project.services.CfdServiceImpl;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class CfdServiceImplITTest {

	@Autowired
	private CfdServiceImpl cfdService;

	@Autowired
	private CfdRepository cfdRepository;

	@Autowired
	private UserRepository userRepository;


	private static Cfd createCfd() {
		Cfd cfd = new Cfd();
		cfd.setId(1L);
		cfd.setName("test");

		return cfd;
	}

	@Test
	public void shouldFindCfd() {
		Cfd cfd = createCfd();
		cfdRepository.save(cfd);

		Cfd savedCfd = cfdService.getCfd(1l);

		assertEquals(1L, savedCfd.getId().longValue());
		assertEquals("test", savedCfd.getName());
	}

	@Test
	public void shouldFindCfds() {
		List<Cfd> savedCfds = cfdService.getAllCfds();

		assertEquals(1, savedCfds.size());
		assertEquals(1L, savedCfds.get(0).getId().longValue());
		assertEquals("test", savedCfds.get(0).getName());
	}
}
