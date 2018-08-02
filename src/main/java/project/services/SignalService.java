package project.services;

import project.models.Signal;

import java.util.List;

public interface SignalService {
	public List<Signal> getSignals();
	public Signal getSignal(Long id);
	public void addSignal(String name, String url);
	public void updateSignal(Long id, String name, String url);
	public void deleteSignal(Long id);
}
