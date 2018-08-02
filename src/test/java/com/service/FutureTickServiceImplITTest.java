package com.service;

import project.Application;
import project.models.Future;
import project.models.FutureTick;
import project.repositories.FutureTickRepository;
import project.repositories.UserRepository;
import project.services.FutureTickServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class FutureTickServiceImplITTest {

	@Autowired
	private FutureTickServiceImpl futureTickService;

	@Autowired
	private FutureTickRepository futureTickRepository;

	@Autowired
	private UserRepository userRepository;

	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	private static FutureTick createFutureTick() throws ParseException {
		Future future = new Future();
		future.setId(1l);
		future.setName("a");

		FutureTick futureTick = new FutureTick();
		futureTick.setId(1l);
		futureTick.setType("C");
		futureTick.setFuture(future);
		futureTick.setMaturity(df.parse("2020-12-19T23:00:00.000Z"));
		futureTick.setStrike(new BigDecimal(2l));
		futureTick.setVol(new BigDecimal(2l));
		futureTick.setFuturePrice(new BigDecimal(2l));
		futureTick.setPrice(new BigDecimal(2l));
		futureTick.setStrike(new BigDecimal(2l));
		futureTick.setT(new BigDecimal(2l));
		futureTick.setR(new BigDecimal(2l));
		futureTick.setGamma(new BigDecimal(2l));
		futureTick.setDelta(new BigDecimal(2l));
		futureTick.setVega(new BigDecimal(2l));
		futureTick.setRho(new BigDecimal(2l));
		futureTick.setTheta(new BigDecimal(2l));
		futureTick.setTimestamp(df.parse("2020-08-14T15:14:29.728Z"));

		return futureTick;
	}

	@Test
	public void shouldFindFutureTick() throws ParseException {
		FutureTick futureTick = createFutureTick();
		futureTickRepository.save(futureTick);

		FutureTick savedFutureTick = futureTickService.getFutureTick(1l);

		assertEquals(1L, savedFutureTick.getId().longValue());
		assertEquals("C", savedFutureTick.getType());
	}

	@Test
	public void shouldFindFutureTicks() {
		List<FutureTick> savedFutureTicks = futureTickService.getFutureTicks(1l);

		assertEquals(1, savedFutureTicks.size());
		assertEquals(1L, savedFutureTicks.get(0).getId().longValue());
		assertEquals("C", savedFutureTicks.get(0).getType());
	}
}
