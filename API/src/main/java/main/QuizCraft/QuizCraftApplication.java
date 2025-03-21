package main.QuizCraft;

import main.QuizCraft.model.deck.Flashcard;
import main.QuizCraft.repository.FlashcardRepository;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.domain.Pageable;

@SpringBootApplication
public class QuizCraftApplication {

	public static void main(String[] args) {
		SpringApplication.run(QuizCraftApplication.class, args);
	}

}
