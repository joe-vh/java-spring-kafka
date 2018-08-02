package project.services;

import project.models.Option;

import java.util.List;

public interface OptionService {
	public List<Option> getAllOptions();
	public Option getOption(long id);
	public void addOption(String name);
	public void updateOption(Long id, String name);
	public void deleteOption(Long id);
}
