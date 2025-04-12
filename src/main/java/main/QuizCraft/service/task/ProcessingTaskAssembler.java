package main.QuizCraft.service.task;

import main.QuizCraft.controller.TaskManagerController;
import main.QuizCraft.dto.ProcessingTaskDto;
import main.QuizCraft.dto.ProcessingTaskStatusDto;
import main.QuizCraft.kafka.ProcessingTask;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;

@Component
public class ProcessingTaskAssembler {

    public ProcessingTaskStatusDto toStatusDTO(ProcessingTask processingTask) {
        ProcessingTaskStatusDto processingTaskStatusDto = new ProcessingTaskStatusDto(
                processingTask.getTaskId(),
                processingTask.getStatus()
        );
        addLinks(processingTaskStatusDto, processingTask.getTaskId());
        return processingTaskStatusDto;
    }

    public ProcessingTaskDto toTaskDto(ProcessingTask processingTask) {
        ProcessingTaskDto processingTaskDto = new ProcessingTaskDto(
                processingTask.getTaskId(),
                processingTask.getMethodProcessingType().toString(),
                processingTask.getStatus().toString(),
                processingTask.getInputParameters(),
                processingTask.getResult(),
                processingTask.getCreatedAt(),
                processingTask.getCompletedAt()
        );
        addLinks(processingTaskDto, processingTask.getTaskId());
        return processingTaskDto;
    }
    private <T extends RepresentationModel> void addLinks(T dto, String taskId) {
        dto.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(TaskManagerController.class)
                                .getTaskStatus(taskId))
                .withRel("status"));

        dto.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(TaskManagerController.class)
                                .getTask(taskId))
                .withRel("object"));

        dto.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(TaskManagerController.class)
                                .getTaskResult(taskId))
                .withRel("result"));
    }
}
