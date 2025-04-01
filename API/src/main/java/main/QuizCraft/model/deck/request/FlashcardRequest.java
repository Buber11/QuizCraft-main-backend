package main.QuizCraft.model.deck.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class FlashcardRequest {

    @NotNull(message = "Front of flashcard have to contain text")
    private String front;

    @NotNull(message = "Back of flashcard have to contain text")
    private String back;

}
