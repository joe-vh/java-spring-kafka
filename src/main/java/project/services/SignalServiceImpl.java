package project.services;

import project.security.UserDetailsFactory;
import project.models.Signal;
import project.models.User;
import project.repositories.SignalRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import java.util.List;

@Service
public class SignalServiceImpl implements SignalService {

	@Autowired
	private SignalRepository signalRepository;

	@Autowired
	EntityManagerFactory entityManagerFactory;

	public List<Signal> getSignals() {
		Long userId = UserDetailsFactory.getId();
		return signalRepository.findByUserId(userId);
	}

	public Signal getSignal(Long id) {
		return signalRepository.findById(id).orElse(null);
	}

	public void addSignal(String name, String url) {
		Long userId = UserDetailsFactory.getId();
		EntityManager em = entityManagerFactory.createEntityManager();
		User user = em.getReference(User.class, userId);

		Signal newSignal = new Signal();
		newSignal.setUser(user);
		newSignal.setName(name);
		newSignal.setUrl(url);

		em.persist(newSignal);
		signalRepository.save(newSignal);
	}

	public void updateSignal(Long id, String name, String url) {
		Signal signal = signalRepository.findById(id).orElse(null);
		signal.setName(name);
		signal.setUrl(url);
		signalRepository.save(signal);
	}

	public void deleteSignal(Long id) {
		Signal signal = signalRepository.findById(id).orElse(null);
		signalRepository.delete(signal);
	}

}
