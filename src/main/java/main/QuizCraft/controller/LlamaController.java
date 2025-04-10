package main.QuizCraft.controller;


import lombok.RequiredArgsConstructor;
import main.QuizCraft.dto.ProcessingTaskStatusDto;
import main.QuizCraft.service.task.AiGenerationProducer;
import main.QuizCraft.kafkaStatus.MethodProcessingType;
import main.QuizCraft.kafkaStatus.ProcessingTask;
import main.QuizCraft.service.task.TaskManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/ai/generation")
@RequiredArgsConstructor
public class LlamaController {

    private final TaskManagerService taskManagerService;
    private final AiGenerationProducer aiGenerationProducer;

    @PostMapping("/quiz")
    public ResponseEntity<ProcessingTaskStatusDto> generateQuiz(@RequestBody String promptMessage) {
        String taskId = taskManagerService.createTask(
                List.of(promptMessage), MethodProcessingType.QUIZ_PROCESSING);
        aiGenerationProducer.sendMessage(taskManagerService.getTask(taskId));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(taskManagerService.getTaskStatusDto(taskId));
    }

    @PostMapping("/flashcard")
    public ResponseEntity<ProcessingTaskStatusDto> generateFlashcard(@RequestBody String promptMessage) {
        String taskId = taskManagerService.createTask(
                List.of(promptMessage), MethodProcessingType.FLASHCARD_PROCESSING);
        aiGenerationProducer.sendMessage(taskManagerService.getTask(taskId));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(taskManagerService.getTaskStatusDto(taskId));
    }

    @PostMapping("/fill-in-the-blanks")
    public ResponseEntity<ProcessingTaskStatusDto> generateFillInTheBlanks(@RequestBody String promptMessage) {
        String taskId = taskManagerService.createTask(
                List.of(promptMessage), MethodProcessingType.FILL_IN_THE_BLANK_PROCESSING);
        aiGenerationProducer.sendMessage(taskManagerService.getTask(taskId));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(taskManagerService.getTaskStatusDto(taskId));
    }

    @PostMapping("/summary")
    public ResponseEntity<ProcessingTaskStatusDto> generateSummary(@RequestBody String promptMessage) {
        String taskId = taskManagerService.createTask(
                List.of(promptMessage), MethodProcessingType.SUMMARY_PROCESSING);
        aiGenerationProducer.sendMessage(taskManagerService.getTask(taskId));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(taskManagerService.getTaskStatusDto(taskId));
    }

    @PostMapping("/true-or-false-questions")
    public ResponseEntity<ProcessingTaskStatusDto> generateTrueFalseQuestions(@RequestBody String promptMessage) {
        String taskId = taskManagerService.createTask(
                List.of(promptMessage), MethodProcessingType.TRUE_FALSE_PROCESSING);
        aiGenerationProducer.sendMessage(taskManagerService.getTask(taskId));
        return ResponseEntity.status(HttpStatus.ACCEPTED).body(taskManagerService.getTaskStatusDto(taskId));
    }
}