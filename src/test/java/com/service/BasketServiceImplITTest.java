package com.service;

import project.Application;
import project.models.Basket;
import project.services.BasketServiceImpl;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class BasketServiceImplITTest {

	@Autowired
	private BasketServiceImpl basketService;

	@Test
	public void shouldSaveBasket() {
		basketService.addBasket("test");
		Basket savedBasket = basketService.getBasket(1l);

		assertEquals(1L, savedBasket.getId().longValue());
		assertEquals("test", savedBasket.getName());
	}

	@Test
	public void shouldFindBaskets() {
		List<Basket> savedBaskets = basketService.getBaskets();

		assertEquals(1, savedBaskets.size());
		assertEquals(1L, savedBaskets.get(0).getId().longValue());
	}
}
