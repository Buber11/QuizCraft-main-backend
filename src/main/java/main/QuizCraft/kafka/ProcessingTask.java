package main.QuizCraft.kafka;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProcessingTask<T> {
    private String taskId;
    private MethodProcessingType methodProcessingType  ;
    private TaskStatus status;
    private Map<String,Object> inputParameters;
    private T result;
    @Min(1)
    @Max(5)
    private int order;
    private ZonedDateTime createdAt;
    private ZonedDateTime completedAt;
    private ZonedDateTime expirationAt;
}
