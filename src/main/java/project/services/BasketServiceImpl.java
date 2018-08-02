package project.services;

import org.springframework.messaging.simp.SimpMessagingTemplate;
import project.models.Basket;
import project.models.User;
import project.repositories.BasketRepository;
import project.repositories.UserRepository;
import project.security.UserDetailsFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManagerFactory;
import java.util.List;

@Service
@Transactional
public class BasketServiceImpl implements BasketService {

	@Autowired
	private BasketRepository basketRepository;

	@Autowired
	EntityManagerFactory entityManagerFactory;

	@Autowired
	SimpMessagingTemplate template;

	@Autowired
	UserRepository userRepository;

	public List<Basket> getBaskets() {
		Long id = UserDetailsFactory.getId();
		return basketRepository.findByUserId(id);
	}

	public Basket getBasket(Long id) {
		Long userId = UserDetailsFactory.getId();
		return basketRepository.findByIdAndUserId(id, userId).orElse(null);
	}

	public void addBasket(String name) {
		Long userId = UserDetailsFactory.getId();
		User user = userRepository.findById(userId).orElse(null);
		Basket newBasket = new Basket();
		newBasket.setUser(user);
		newBasket.setName(name);
		basketRepository.save(newBasket);
		template.convertAndSend("/topic/reload/" + userId, "Basket added");
	}

	public void updateBasket(Long id, String name) {
		Basket basket = basketRepository.findById(id).orElse(null);
		basket.setName(name);
		basketRepository.save(basket);
	}

	public void deleteBasket(Long id) {
		Basket basket = basketRepository.findById(id).orElse(null);
		basketRepository.delete(basket);
	}

}
