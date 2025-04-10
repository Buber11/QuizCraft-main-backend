package main.QuizCraft.service.task;

import main.QuizCraft.dto.ProcessingTaskDto;
import main.QuizCraft.dto.ProcessingTaskStatusDto;
import main.QuizCraft.kafkaStatus.MethodProcessingType;
import main.QuizCraft.kafkaStatus.ProcessingTask;

import java.util.List;

public interface TaskManagerService {
    String createTask(List parametres, MethodProcessingType methodProcessingType);
    ProcessingTask getTask(String taskId);
    void updateTask(ProcessingTask processingTask);
    ProcessingTaskDto getTaskDto(String taskId);
    ProcessingTaskStatusDto getTaskStatusDto(String taskId);
}
