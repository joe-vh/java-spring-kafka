package com.service;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import project.Application;
import project.models.Option;
import project.repositories.OptionRepository;
import project.repositories.UserRepository;
import project.services.OptionServiceImpl;

import java.util.List;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {Application.class})
public class OptionServiceImplITTest {

	@Autowired
	private OptionServiceImpl optionService;

	@Autowired
	private OptionRepository optionRepository;

	@Autowired
	private UserRepository userRepository;


	private static Option createOption() {
		Option option = new Option();
		option.setId(1L);
		option.setName("test");

		return option;
	}

	@Test
	public void shouldFindOption() {
		Option option = createOption();
		optionRepository.save(option);

		Option savedOption = optionService.getOption(1l);

		assertEquals(1L, savedOption.getId().longValue());
		assertEquals("test", savedOption.getName());
	}

	@Test
	public void shouldFindOptions() {
		List<Option> savedOptions = optionService.getAllOptions();

		assertEquals(1, savedOptions.size());
		assertEquals(1L, savedOptions.get(0).getId().longValue());
		assertEquals("test", savedOptions.get(0).getName());
	}
}
