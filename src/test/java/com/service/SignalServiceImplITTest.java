package com.service;

import project.Application;
import project.models.Signal;
import project.services.SignalServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class SignalServiceImplITTest {

	@Autowired
	private SignalServiceImpl signalService;

	@Test
	public void shouldSaveSignal() {
		signalService.addSignal("test", "abc.com");
		Signal savedSignal = signalService.getSignal(1l);

		assertEquals(1L, savedSignal.getId().longValue());
		assertEquals("test", savedSignal.getName());
		assertEquals("abc.com", savedSignal.getUrl());
	}

	@Test
	public void shouldFindSignals() {
		List<Signal> savedSignals = signalService.getSignals();

		assertEquals(1, savedSignals.size());
		assertEquals(1L, savedSignals.get(0).getId().longValue());
		assertEquals("test", savedSignals.get(0).getName());
		assertEquals("abc.com", savedSignals.get(0).getUrl());
	}
}
