package project.services;

import project.models.Cfd;
import project.repositories.CfdRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CfdServiceImpl implements CfdService {

	@Autowired
	private CfdRepository cfdRepository;

	public List<Cfd> getAllCfds() {
		return cfdRepository.findAll();
	}

	public Cfd getCfd(Long id) {
		return cfdRepository.findById(id).orElse(null);
	}

	public void addCfd(String name) {
		Cfd newCfd = new Cfd();
		newCfd.setName(name);
		cfdRepository.save(newCfd);
	}

	public void updateCfd(Long id, String name) {
		Cfd cfd = cfdRepository.findById(id).orElse(null);
		cfd.setName(name);
		cfdRepository.save(cfd);
	}

	public void deleteCfd(Long id) {
		Cfd cfd = cfdRepository.findById(id).orElse(null);
		cfdRepository.delete(cfd);
	}

}
