package project.services;

import project.repositories.ConditionRepository;
import project.security.UserDetailsFactory;
import project.models.Condition;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ConditionServiceImpl implements ConditionService {

	@Autowired
	private ConditionRepository conditionRepository;

	public List<Condition> getConditions() {
		Long userId = UserDetailsFactory.getId();
		return conditionRepository.findByUserIdAndOccurred(userId, false);
	}
}
