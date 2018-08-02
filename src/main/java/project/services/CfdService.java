package project.services;

import project.models.Cfd;

import java.util.List;

public interface CfdService {
	public List<Cfd> getAllCfds();
	public Cfd getCfd(Long id);
	public void addCfd(String name);
	public void updateCfd(Long id, String name);
	public void deleteCfd(Long id);
}
