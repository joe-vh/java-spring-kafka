package project.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.json.JSONException;

public interface ElasticSearchService {
	public void updateRecord(Long userId) throws JsonProcessingException, JSONException;
}
