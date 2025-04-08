package main.QuizCraft.request;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

import javax.validation.constraints.NotNull;

@Data
public class QuizRequest {

    @NotBlank(message = "Question cannot be blank")
    @Size(max = 255, message = "Question cannot exceed 255 characters")
    private String question;

    @NotBlank(message = "Correct answer cannot be blank")
    @Size(max = 255, message = "Correct answer cannot exceed 255 characters")
    private String correctAnswer;

    @NotBlank(message = "Bad answer 1 cannot be blank")
    @Size(max = 255, message = "Bad answer 1 cannot exceed 255 characters")
    private String badAnswer1;

    @NotBlank(message = "Bad answer 2 cannot be blank")
    @Size(max = 255, message = "Bad answer 2 cannot exceed 255 characters")
    private String badAnswer2;

    @NotBlank(message = "Bad answer 3 cannot be blank")
    @Size(max = 255, message = "Bad answer 3 cannot exceed 255 characters")
    private String badAnswer3;

    @NotNull(message = "Deck ID cannot be null")
    private Long deckId;
}
