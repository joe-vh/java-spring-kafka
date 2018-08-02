package project.services;

import project.models.Future;
import project.repositories.FutureRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class FutureServiceImpl implements FutureService {

	@Autowired
	private FutureRepository futureRepository;

	public List<Future> getAllFutures() {
		return futureRepository.findAll();
	}

	public Future getFuture(Long id) {
		return futureRepository.findById(id).orElse(null);
	}

	public void addFuture(String name) {
		Future newFuture = new Future();
		newFuture.setName(name);
		futureRepository.save(newFuture);
	}

	public void updateFuture(Long id, String name) {
		Future future = futureRepository.findById(id).orElse(null);
		future.setName(name);
		futureRepository.save(future);
	}

	public void deleteFuture(Long id) {
		Future future = futureRepository.findById(id).orElse(null);
		futureRepository.delete(future);
	}

}
