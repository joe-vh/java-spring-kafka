package project.services;

import project.models.Option;

import javax.script.ScriptException;
import java.io.IOException;
import java.text.ParseException;
import java.util.List;

public interface KafkaService {
	public void consume(String message) throws Exception, IOException, ParseException, ScriptException;
}
