package project.services;

import project.models.Strategy;
import project.payload.request.ConditionTemplate;

import java.util.List;

public interface StrategyService {
	public List<Strategy> getStrategies();
	public Strategy getStrategy(Long id);
	public List<Strategy> getStrategiesByBasketId(Long basketId);
	public void addStrategy(
		String name,
		Long basketId,
		Integer quantity,
		Boolean side,
		Boolean open,
		Long futureId,
		Long optionId,
		Long cfdId,
		List<ConditionTemplate> conditions
	);
	public void deleteStrategy(Long id);
}
