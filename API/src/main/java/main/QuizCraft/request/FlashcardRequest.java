package main.QuizCraft.request;

import lombok.AllArgsConstructor;
import lombok.Data;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class FlashcardRequest {

    @NotNull(message = "deck id of flashcard have to contain text")
    @Pattern(regexp = "[0-9]",message = "deck id have to be number")
    private Long deckId;

    @NotNull(message = "Front of flashcard have to contain text")
    private String front;

    @NotNull(message = "Back of flashcard have to contain text")
    private String back;


}
