package project.services;

import project.models.Strategy;
import project.repositories.StrategyRepository;
import project.repositories.UserRepository;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import project.models.User;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Service
public class ElasticSearchServiceImpl implements ElasticSearchService {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private StrategyRepository strategyRepository;

	public void updateRecord(Long userId) throws JsonProcessingException, JSONException {
		User userResult = userRepository.findById(userId).orElse(null);
		List<Strategy> strategyResults = strategyRepository.findByUserIdAndOccurred(userId, false);
		ObjectMapper objectMapper = new ObjectMapper();
		objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

		String userJson = objectMapper.writeValueAsString(
				new Object() {
					public final User user = userResult;
					public final List<Strategy> strategies = strategyResults;
				}
			);

		String url = "http://0.0.0.0:9200/search/data/" + userResult.getId().toString();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);

		JSONObject request = new JSONObject();
		request.put("document", userJson);

		HttpEntity<String> entity = new HttpEntity<String>(request.toString(), headers);
		new RestTemplate().postForObject(url, entity, String.class);
	}

}
