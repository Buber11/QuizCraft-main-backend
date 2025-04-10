package main.QuizCraft.kafkaStatus;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ProcessingTask<T> {
    private String taskId;
    private MethodProcessingType methodProcessingType  ;
    private TaskStatus status;
    private List inputParameters;
    private T result;
    private Instant createdAt;
    private Instant completedAt;
}
