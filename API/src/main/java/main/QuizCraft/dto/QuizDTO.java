package main.QuizCraft.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class QuizDTO extends RepresentationModel<QuizDTO> {
    private Long id;
    private String question;
    private String correctAnswer;
    private String badAnswer1;
    private String badAnswer2;
    private String badAnswer3;
}
