package main.QuizCraft.service.task;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.dto.ProcessingTaskDto;
import main.QuizCraft.dto.ProcessingTaskStatusDto;
import main.QuizCraft.exception.ProcessingTaskException;
import main.QuizCraft.kafka.*;
import main.QuizCraft.mapStruct.ExpirationConcurrentHashMap;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class TaskManagerServiceImpl implements TaskManagerService{

    private final Map<String, ProcessingTask> tasks = new ExpirationConcurrentHashMap();
    private final ProcessingTaskAssembler processingTaskAssembler;
    private final KafkaProducer kafkaProducer;

    @Override
    public ProcessingTaskStatusDto createTask(Map<String,Object> parametres,
                             HttpServletRequest request,
                             TOPIC topic,
                             MethodProcessingType methodProcessingType) {

        Long userId = (Long) request.getAttribute("user_id");
        if (userId == null) {
            throw new ProcessingTaskException("User ID not found in request attributes");
        }
        int orderOfTask = getOrderOfTask(userId);

        String taskId = UUID.randomUUID().toString();
        ProcessingTask task = new ProcessingTask(
                taskId,
                methodProcessingType,
                TaskStatus.PENDING,
                parametres,
                null,
                orderOfTask,
                Instant.now(),
                null,
                null
        );

        tasks.put(taskId, task);
        kafkaProducer.sendMessage(task,topic);
        return processingTaskAssembler.toStatusDTO(task);
    }

    private int getOrderOfTask(Long userId) {
        return 1;
        //ToDO mechanism for ordering tasks
//        return switch (task.getMethodProcessingType()) {
//            case QUIZ_PROCESSING -> 1;
//            case FLASHCARD_PROCESSING -> 2;
//            case FILL_IN_THE_BLANK_PROCESSING -> 3;
//            case SUMMARY_PROCESSING -> 4;
//            case TRUE_FALSE_PROCESSING -> 5;
//            default -> throw new IllegalArgumentException("Unknown processing type: " + task.getMethodProcessingType());
//        };
    }

    @Override
    public ProcessingTask getTask(String taskId) {
        var task = tasks.get(taskId);
        if(task != null) {
            return task;
        } else {
            throw new ProcessingTaskException(String.format("Task with ID %s not found", taskId));
        }
    }

    @Async
    @Override
    public void updateTask(ProcessingTask processingTask) {
        tasks.put(processingTask.getTaskId(), processingTask);
    }

    @Override
    public ProcessingTaskDto getTaskDto(String taskId) {
        ProcessingTask processingTask = tasks.get(taskId);
        return processingTaskAssembler.toTaskDto(processingTask);
    }

    @Override
    public ProcessingTaskStatusDto getTaskStatusDto(String taskId) {
        ProcessingTask processingTask = tasks.get(taskId);
        return processingTaskAssembler.toStatusDTO(processingTask);
    }

    @Override
    public <T> T getTaskResult(String taskId) {
        ProcessingTask processingTask = tasks.get(taskId);
        if(processingTask.getResult() != null) {
            return (T) processingTask.getResult();
        }else {
            throw new ProcessingTaskException(String.format("Result not found for task with ID %s", taskId));
        }
    }
}
