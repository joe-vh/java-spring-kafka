package com.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.Application;
import project.models.Cfd;
import project.models.CfdTick;
import project.repositories.CfdTickRepository;
import project.repositories.UserRepository;
import project.services.CfdTickServiceImpl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class CfdTickServiceImplITTest {

	@Autowired
	private CfdTickServiceImpl cfdTickService;

	@Autowired
	private CfdTickRepository cfdTickRepository;

	@Autowired
	private UserRepository userRepository;

	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	private static CfdTick createCfdTick() throws ParseException {
		Cfd cfd = new Cfd();
		cfd.setId(1l);
		cfd.setName("a");

		CfdTick cfdTick = new CfdTick();
		cfdTick.setId(1l);
		cfdTick.setBuy(new BigDecimal(1l));
		cfdTick.setSell(new BigDecimal(3l));
		cfdTick.setCfd(cfd);
		cfdTick.setPrice(new BigDecimal(2l));
		cfdTick.setTimestamp(df.parse("2020-08-14T15:14:29.728Z"));

		return cfdTick;
	}

	@Test
	public void shouldFindCfdTick() throws ParseException {
		CfdTick cfdTick = createCfdTick();
		cfdTickRepository.save(cfdTick);

		CfdTick savedCfdTick = cfdTickService.getCfdTick(1l);

		assertEquals(1L, savedCfdTick.getId().longValue());
		assertEquals(3L, savedCfdTick.getSell());
	}

	@Test
	public void shouldFindCfdTicks() {
		List<CfdTick> savedCfdTicks = cfdTickService.getCfdTicks(1l);

		assertEquals(1, savedCfdTicks.size());
		assertEquals(1L, savedCfdTicks.get(0).getId().longValue());
		assertEquals(3L, savedCfdTicks.get(0).getSell());
	}
}
