package com.service;

import project.Application;
import project.enums.EInstrument;
import project.models.Position;
import project.services.PositionServiceImpl;
import org.json.JSONException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class PositionServiceImplITTest {

	@Autowired
	private PositionServiceImpl positionService;

	@Test
	public void shouldSavePosition() throws JSONException {
		positionService.createPosition(
				1l,
				1l,
				1l,
				1l,
				4,
				new BigDecimal(90),
				null,
				"future",
				"C",
				new Date()
		);

		Position savedPosition = positionService.getPosition(1l);

		assertEquals(1l, savedPosition.getId().longValue());
		assertEquals(1l, savedPosition.getFuture().getId().longValue());
		assertEquals(1l, savedPosition.getUser().getId().longValue());
		assertEquals(1l, savedPosition.getBasket().getId().longValue());
	}

	@Test
	public void shouldFindPosition() {
		Position savedPosition = positionService.getPosition(1l);

		assertEquals(1l, savedPosition.getId().longValue());
		assertEquals(1l, savedPosition.getFuture().getId().longValue());
		assertEquals(1l, savedPosition.getUser().getId().longValue());
		assertEquals(1l, savedPosition.getBasket().getId().longValue());
	}

	@Test
	public void shouldFindPositionsByBasket() {
		List<Position> positions = positionService.getAllPositionsByBasket(1l);

		assertEquals(1l, positions.get(0).getId().longValue());
		assertEquals(1l, positions.get(0).getFuture().getId().longValue());
		assertEquals(1l, positions.get(0).getUser().getId().longValue());
		assertEquals(1l, positions.get(0).getBasket().getId().longValue());
	}


	@Test
	public void shouldClosePosition() throws JSONException {
		positionService.closePosition(
				1l,
				1l,
				4,
				null,
				EInstrument.FUTURE,
				1l,
				null,
				null,
				"C",
				new Date()
		);

		Position savedPosition = positionService.getPosition(1l);

		assertEquals(1l, savedPosition.getId().longValue());
		assertEquals(0, savedPosition.getQuantity().intValue());
	}

	@Test
	public void shouldFindPositions() {
		List<Position> savedPositions = positionService.getPositions();

		assertEquals(1, savedPositions.size());
		assertEquals(1L, savedPositions.get(0).getId().longValue());
	}
}
