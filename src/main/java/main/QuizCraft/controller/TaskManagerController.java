package main.QuizCraft.controller;

import lombok.RequiredArgsConstructor;
import main.QuizCraft.dto.ProcessingTaskDto;
import main.QuizCraft.dto.ProcessingTaskStatusDto;
import main.QuizCraft.service.task.TaskManagerService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/task")
@RequiredArgsConstructor
public class TaskManagerController {

    private final TaskManagerService taskManagerService;

    @GetMapping("/{taskId}")
    public ResponseEntity<ProcessingTaskStatusDto> getTaskStatus(@PathVariable String taskId) {
        return ResponseEntity.ok(taskManagerService.getTaskStatusDto(taskId));
    }

    @GetMapping("{taskId}/status")
    public ResponseEntity<ProcessingTaskDto> getTask(@PathVariable String taskId) {
        return ResponseEntity.ok(taskManagerService.getTaskDto(taskId));
    }

    @GetMapping("/{taskId}/result")
    public ResponseEntity<?> getTaskResult(@PathVariable String taskId) {
        return ResponseEntity.ok(taskManagerService.getTaskResult(taskId));
    }
}
