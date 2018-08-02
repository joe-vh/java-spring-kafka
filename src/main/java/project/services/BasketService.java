package project.services;

import project.models.Basket;

import java.util.List;

public interface BasketService {
	public List<Basket> getBaskets();
	public Basket getBasket(Long id);
	public void addBasket(String name);
	public void updateBasket(Long id, String name);
	public void deleteBasket(Long id);
}
