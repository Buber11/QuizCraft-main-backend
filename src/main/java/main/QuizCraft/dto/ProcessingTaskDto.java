package main.QuizCraft.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.hateoas.RepresentationModel;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProcessingTaskDto<T> extends RepresentationModel<ProcessingTaskStatusDto> {
    private String taskId;
    private String methodProcessingType;
    private String status;
    private Map<String,Object> inputParameters;
    private T result;
    private ZonedDateTime createdAt;
    private ZonedDateTime completedAt;
}
