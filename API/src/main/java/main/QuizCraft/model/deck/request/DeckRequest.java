package main.QuizCraft.model.deck.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Pattern;

@Data
@AllArgsConstructor
public class DeckRequest {

    @NotNull
    @Pattern(regexp = "[a-zA-Z0-9]+", message = "Name must contain only letters and numbers")
    @Length(max = 255, message = "Your name for deck is too long")
    private String name;

    @NotNull
    @Pattern(regexp = "[1-9]+", message = "UserId must contain only digits from 1 to 9")
    private Long userId;


}
