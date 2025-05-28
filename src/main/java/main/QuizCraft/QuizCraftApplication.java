package main.QuizCraft;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class QuizCraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizCraftApplication.class, args);
	}

}
