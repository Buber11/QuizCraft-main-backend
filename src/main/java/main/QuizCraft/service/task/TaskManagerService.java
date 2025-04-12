package main.QuizCraft.service.task;

import jakarta.servlet.http.HttpServletRequest;
import main.QuizCraft.dto.ProcessingTaskDto;
import main.QuizCraft.dto.ProcessingTaskStatusDto;
import main.QuizCraft.kafka.MethodProcessingType;
import main.QuizCraft.kafka.ProcessingTask;
import main.QuizCraft.kafka.TOPIC;

import java.util.List;
import java.util.Map;

public interface TaskManagerService {
    ProcessingTaskStatusDto createTask(Map<String,Object> parametres,
                                       HttpServletRequest request,
                                       TOPIC topic,
                                       MethodProcessingType methodProcessingType);
    ProcessingTask getTask(String taskId);
    void updateTask(ProcessingTask processingTask);
    ProcessingTaskDto getTaskDto(String taskId);
    ProcessingTaskStatusDto getTaskStatusDto(String taskId);
    <T> T getTaskResult(String taskId);
}
