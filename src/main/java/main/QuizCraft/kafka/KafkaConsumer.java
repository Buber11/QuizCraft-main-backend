package main.QuizCraft.kafka;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.QuizCraft.dto.FlashcardDTO;
import main.QuizCraft.dto.QuizDTO;
import main.QuizCraft.response.MessageResponse;
import main.QuizCraft.service.Llama.LlamaAiService;
import main.QuizCraft.service.task.TaskManagerService;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.Acknowledgment;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.PriorityBlockingQueue;

@Service
@Slf4j
@RequiredArgsConstructor
public class KafkaConsumer {

    private final LlamaAiService llamaAiService;
    private final TaskManagerService taskManagerService;
    private final ObjectMapper objectMapper;

    private final PriorityBlockingQueue<ProcessingTask<?>> taskQueue =
            new PriorityBlockingQueue<>(20,
                    Comparator.comparingInt((ProcessingTask<?> task) -> task.getOrder()).reversed());

    @PostConstruct
    public void init() {
        Executors.newSingleThreadExecutor().submit(() -> {
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    ProcessingTask<?> task = taskQueue.take();
                    if(task.getStatus() != TaskStatus.FAILED){
                        processTask(task);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    log.error("Queue processing interrupted", e);
                }
            }
        });
    }


    @Async
    @KafkaListener(topics = "ai-response-for-vector-data",
            groupId = "ai-consumer-group",
            clientIdPrefix = "java-client-response")
    public void listen(String message, Acknowledgment ack) {
        log.info("Received Kafka message: {}", message);
        try {
            ProcessingTask<?> processingTask = objectMapper.readValue(message, ProcessingTask.class);
            taskQueue.put(processingTask);
            ack.acknowledge();
        } catch (JsonProcessingException e) {
            log.error("Error parsing Kafka message: {}", e.getMessage(), e);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Unexpected error while processing Kafka message: {}", e.getMessage(), e);
        }
    }

    public void processTask(ProcessingTask processingTask){
        Map map = (Map) processingTask.getResult();
        List contex = (List) map.get("sentences");
        try {
            switch (processingTask.getMethodProcessingType()) {
                case MethodProcessingType.QUIZ_PROCESSING:
                    List<QuizDTO> quizDTOList = llamaAiService
                            .generateQuiz(contex);
                    processingTask.setResult(quizDTOList);
                    break;
                case MethodProcessingType.FLASHCARD_PROCESSING:
                    List<FlashcardDTO> flashcardDTOS = llamaAiService
                            .generateFlashcards(contex);
                    processingTask.setResult(flashcardDTOS);
                    break;
                case MethodProcessingType.FILL_IN_THE_BLANK_PROCESSING:
                    MessageResponse generateFillInTheBlanks = llamaAiService
                            .generateFillInTheBlanks(contex);
                    processingTask.setResult(generateFillInTheBlanks);
                    break;
                case MethodProcessingType.SUMMARY_PROCESSING:
                    MessageResponse generateSummary = llamaAiService
                            .generateSummary(contex);
                    processingTask.setResult(generateSummary);
                    break;
                case MethodProcessingType.TRUE_FALSE_PROCESSING:
                    MessageResponse generateTrueFalseQuestions = llamaAiService
                            .generateTrueFalseQuestions(contex);
                    processingTask.setResult(generateTrueFalseQuestions);
                    break;
                default:
                    log.warn("Unknown message type received from Kafka: {}", processingTask.getMethodProcessingType());
            }

            processingTask.setStatus(TaskStatus.COMPLETED);
            processingTask.setCompletedAt(Instant.now());
            processingTask.setExpirationAt(processingTask.getCompletedAt().plus(5, ChronoUnit.MINUTES));

            taskManagerService.updateTask(processingTask);
            log.info("Task completed successfully: taskId={}", processingTask.getTaskId());
        } catch (Exception e) {
            processingTask.setStatus(TaskStatus.FAILED);
            taskManagerService.updateTask(processingTask);
            log.error("Error processing task {}: {}", processingTask.getTaskId(), e.getMessage(), e);
        }
    }

    @Async
    @KafkaListener(topics = "status-processing",
            groupId = "ai-status-group",
            clientIdPrefix = "java-client-status")
    public void listenStatus(String message, Acknowledgment ack) {
        log.info("Received Kafka message: {}", message);
        try {
            ProcessingTask<?> processingTask = objectMapper.readValue(message, ProcessingTask.class);
            taskManagerService.updateTask(processingTask);
            ack.acknowledge();
        } catch (JsonProcessingException e) {
            log.error("Error parsing Kafka message: {}", e.getMessage(), e);
            ack.acknowledge();
        } catch (Exception e) {
            log.error("Unexpected error while processing Kafka message: {}", e.getMessage(), e);
        }
    }
}
