package com.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.Application;
import project.models.Option;
import project.models.OptionTick;
import project.repositories.OptionTickRepository;
import project.repositories.UserRepository;
import project.services.OptionTickServiceImpl;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class OptionTickServiceImplITTest {

	@Autowired
	private OptionTickServiceImpl optionTickService;

	@Autowired
	private OptionTickRepository optionTickRepository;

	@Autowired
	private UserRepository userRepository;

	static DateFormat df = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX");

	private static OptionTick createOptionTick() throws ParseException {
		Option option = new Option();
		option.setId(1l);
		option.setName("a");

		OptionTick optionTick = new OptionTick();
		optionTick.setId(1l);
		optionTick.setType("C");
		optionTick.setOption(option);
		optionTick.setMaturity(df.parse("2020-12-19T23:00:00.000Z"));
		optionTick.setStrike(new BigDecimal(2l));
		optionTick.setVol(new BigDecimal(2l));
		optionTick.setSpot(new BigDecimal(2l));
		optionTick.setPrice(new BigDecimal(2l));
		optionTick.setStrike(new BigDecimal(2l));
		optionTick.setT(new BigDecimal(2l));
		optionTick.setR(new BigDecimal(2l));
		optionTick.setGamma(new BigDecimal(2l));
		optionTick.setDelta(new BigDecimal(2l));
		optionTick.setVega(new BigDecimal(2l));
		optionTick.setRho(new BigDecimal(2l));
		optionTick.setTheta(new BigDecimal(2l));
		optionTick.setTimestamp(df.parse("2020-08-14T15:14:29.728Z"));

		return optionTick;
	}

	@Test
	public void shouldFindOptionTick() throws ParseException {
		OptionTick optionTick = createOptionTick();
		optionTickRepository.save(optionTick);

		OptionTick savedOptionTick = optionTickService.getOptionTick(1l);

		assertEquals(1L, savedOptionTick.getId().longValue());
		assertEquals("C", savedOptionTick.getType());
	}

	@Test
	public void shouldFindOptionTicks() {
		List<OptionTick> savedOptionTicks = optionTickService.getOptionTicks(1l);

		assertEquals(1, savedOptionTicks.size());
		assertEquals(1L, savedOptionTicks.get(0).getId().longValue());
		assertEquals("C", savedOptionTicks.get(0).getType());
	}
}
