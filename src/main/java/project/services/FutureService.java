package project.services;

import project.models.Future;

import java.util.List;

public interface FutureService {
	public List<Future> getAllFutures();
	public Future getFuture(Long id);
	public void addFuture(String name);
	public void updateFuture(Long id, String name);
	public void deleteFuture(Long id);
}
