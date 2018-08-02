package project;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
//import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;

//@SpringBootApplication(exclude = org.springframework.boot.autoconfigure.security.SecurityDataConfiguration.class)
@EnableAutoConfiguration
@ComponentScan("project")
public class Application {

	public static void main(String[] args) {
		org.springframework.boot.SpringApplication.run(Application.class, args);
	}

}
