package project.services;

import project.models.Option;
import project.repositories.OptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OptionServiceImpl implements OptionService {

	@Autowired
	private OptionRepository optionRepository;

	public List<Option> getAllOptions() {
		return optionRepository.findAll();
	}

	public Option getOption(long id) {
		return optionRepository.findById(id).orElse(null);
	}

	public void addOption(String name) {
		Option newOption = new Option();
		newOption.setName(name);
		optionRepository.save(newOption);
	}

	public void updateOption(Long id, String name) {
		Option option = optionRepository.findById(id).orElse(null);
		option.setName(name);
		optionRepository.save(option);
	}

	public void deleteOption(Long id) {
		Option option = optionRepository.findById(id).orElse(null);
		optionRepository.delete(option);
	}

}
