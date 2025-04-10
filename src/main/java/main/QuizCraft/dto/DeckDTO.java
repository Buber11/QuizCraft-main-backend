package main.QuizCraft.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
public class DeckDTO extends RepresentationModel<DeckDTO> {
    private Long id;
    private String name;
}
