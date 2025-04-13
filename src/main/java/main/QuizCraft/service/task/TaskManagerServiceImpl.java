package main.QuizCraft.service.task;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.QuizCraft.dto.ProcessingTaskDto;
import main.QuizCraft.dto.ProcessingTaskStatusDto;
import main.QuizCraft.exception.ProcessingTaskException;
import main.QuizCraft.kafka.*;
import main.QuizCraft.mapStruct.ExpirationConcurrentHashMap;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class TaskManagerServiceImpl implements TaskManagerService{

    private final Map<String, ProcessingTask> tasks = new ExpirationConcurrentHashMap();
    private final ProcessingTaskAssembler processingTaskAssembler;
    private final KafkaProducer kafkaProducer;

    @Override
    public ProcessingTaskStatusDto createTask(Map<String, Object> parametres,
                                              HttpServletRequest request,
                                              TOPIC topic,
                                              MethodProcessingType methodProcessingType) {
        log.info("Starting task creation with parameters: {}, topic: {}, methodProcessingType: {}", parametres, topic, methodProcessingType);

        Long userId = (Long) request.getAttribute("user_id");
        if (userId == null) {
            log.error("User ID not found in request attributes");
            throw new ProcessingTaskException("User ID not found in request attributes");
        }
        log.info("User ID retrieved: {}", userId);

        int orderOfTask = getOrderOfTask(userId);
        log.info("Order of task determined: {}", orderOfTask);

        parametres.put("user_id", userId);
        String taskId = UUID.randomUUID().toString();
        log.info("Generated task ID: {}", taskId);

        ProcessingTask task = new ProcessingTask(
                taskId,
                methodProcessingType,
                TaskStatus.PENDING,
                parametres,
                null,
                orderOfTask,
                ZonedDateTime.now(),
                null,
                null
        );
        log.debug("Created ProcessingTask object: {}", task);

        tasks.put(taskId, task);
        log.info("Task added to the task map: {}", taskId);

        kafkaProducer.sendMessage(task, topic);
        log.info("Task sent to Kafka topic: {}", topic);

        ProcessingTaskStatusDto statusDto = processingTaskAssembler.toStatusDTO(task);
        log.info("Task status DTO created: {}", statusDto);

        return statusDto;
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
