package main.QuizCraft.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.dto.ProcessingTaskStatusDto;
import main.QuizCraft.kafka.MethodProcessingType;
import main.QuizCraft.kafka.TOPIC;
import main.QuizCraft.service.task.TaskManagerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("api/v1/ai/generation")
@RequiredArgsConstructor
public class LlamaController {

    private final TaskManagerService taskManagerService;

    @PostMapping("/{type}")
    public ResponseEntity<ProcessingTaskStatusDto> generateContent(
            @PathVariable("type") String type,
            HttpServletRequest request,
            @RequestBody String promptMessage) {
        System.out.println("wełszo");
        MethodProcessingType processingType = mapToProcessingType(type);
        ProcessingTaskStatusDto taskStatusDto = taskManagerService.createTask(
                new HashMap<>(Map.of("prompt",promptMessage)),
                request,
                TOPIC.AI_REQUEST_FOR_VECTOR_DATA,
                processingType);

        return ResponseEntity.status(HttpStatus.ACCEPTED).body(taskStatusDto);
    }

    private MethodProcessingType mapToProcessingType(String type) {
        return switch (type.toLowerCase()) {
            case "quiz" -> MethodProcessingType.QUIZ_PROCESSING;
            case "flashcard" -> MethodProcessingType.FLASHCARD_PROCESSING;
            case "fill-in-the-blanks" -> MethodProcessingType.FILL_IN_THE_BLANK_PROCESSING;
            case "summary" -> MethodProcessingType.SUMMARY_PROCESSING;
            case "true-or-false-questions" -> MethodProcessingType.TRUE_FALSE_PROCESSING;
            default -> throw new IllegalArgumentException("Invalid processing type: " + type);
        };
    }
};


