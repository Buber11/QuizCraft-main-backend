package main.QuizCraft.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import main.QuizCraft.kafka.TaskStatus;
import org.springframework.hateoas.RepresentationModel;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessingTaskStatusDto extends RepresentationModel<ProcessingTaskStatusDto> {
    private String taskId;
    private TaskStatus status;
}
