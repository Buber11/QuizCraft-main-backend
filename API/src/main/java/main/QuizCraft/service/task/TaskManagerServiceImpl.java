package main.QuizCraft.service.task;

import lombok.RequiredArgsConstructor;
import main.QuizCraft.dto.ProcessingTaskDto;
import main.QuizCraft.dto.ProcessingTaskStatusDto;
import main.QuizCraft.exception.ProcessingTaskException;
import main.QuizCraft.kafkaStatus.MethodProcessingType;
import main.QuizCraft.kafkaStatus.ProcessingTask;
import main.QuizCraft.kafkaStatus.TaskStatus;
import main.QuizCraft.mapStruct.ExpirationConcurrentHashMap;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

@Service
@RequiredArgsConstructor
public class TaskManagerServiceImpl implements TaskManagerService{

    private final Map<String, ProcessingTask> tasks = new ExpirationConcurrentHashMap();
    private final ProcessingTaskAssembler processingTaskAssembler;

    @Override
    public String createTask(List parametres, MethodProcessingType methodProcessingType) {
        String taskId = UUID.randomUUID().toString();
        ProcessingTask task = new ProcessingTask(
                taskId,
                methodProcessingType,
                TaskStatus.PENDING,
                parametres,
                null,
                Instant.now(),
                null,
                null
        );
        tasks.put(taskId, task);
        return taskId;
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
