package project.services;

import project.repositories.StrategyRepository;
import project.repositories.UserRepository;
import project.models.Strategy;
import project.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManagerFactory;
import java.util.ArrayList;
import java.util.List;

@Service
public class UserServiceImpl implements UserService {

	@Autowired
	private StrategyRepository strategyRepository;

	@Autowired
	private UserRepository userRepository;

	@Autowired
	EntityManagerFactory entityManagerFactory;

	public List<Object> getAllUsersAndStrategiesByUsername(String username) {
		List<Object> result = new ArrayList<>();
		Iterable<User> users = userRepository.findByUsernameKeyword(username);
		for (User u : users) {
			// find strategies, conditions, positions
			List<Strategy> s = strategyRepository.findByUserIdAndOccurred(u.getId(), false);
			result.add(new Object() {
				public final Long id = u.getId();
				public final String username = u.getUsername();
				public final List<Strategy> strategies = s;
			});
		}

		return result;
	}
}
