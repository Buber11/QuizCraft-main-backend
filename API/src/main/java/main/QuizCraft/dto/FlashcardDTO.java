package main.QuizCraft.dto;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class FlashcardDTO extends RepresentationModel<FlashcardDTO> {
    private Long id;
    private String front;
    private String back;

}
