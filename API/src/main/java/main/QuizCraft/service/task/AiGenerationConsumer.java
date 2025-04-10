package main.QuizCraft.service.task;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.QuizCraft.dto.FlashcardDTO;
import main.QuizCraft.dto.QuizDTO;
import main.QuizCraft.kafkaStatus.MethodProcessingType;
import main.QuizCraft.kafkaStatus.ProcessingTask;
import main.QuizCraft.kafkaStatus.TaskStatus;
import main.QuizCraft.response.MessageResponse;
import main.QuizCraft.service.Llama.LlamaAiService;
import org.apache.logging.log4j.CloseableThreadContext;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class AiGenerationConsumer {

    private final LlamaAiService llamaAiService;
    private final TaskManagerService taskManagerService;

    @Async
    @KafkaListener(topics = "ai-generation-requests", groupId = "ai-consumer-group")
    public void listen(String taskId) {
        log.info("Received Kafka message: Type={}, taskId={}", taskId);

        ProcessingTask processingTask = taskManagerService.getTask(taskId);
        processingTask.setStatus(TaskStatus.PROCESSING);
        taskManagerService.updateTask(processingTask);
        try {
            switch (processingTask.getMethodProcessingType()) {
                case MethodProcessingType.QUIZ_PROCESSING:
                    List<QuizDTO> quizDTOList = llamaAiService
                            .generateQuiz((String) processingTask.getInputParameters().get(0));
                    processingTask.setResult(quizDTOList);
                    break;
                case MethodProcessingType.FLASHCARD_PROCESSING:
                    List<FlashcardDTO> flashcardDTOS = llamaAiService
                            .generateFlashcards((String) processingTask.getInputParameters().get(0));
                    processingTask.setResult(flashcardDTOS);
                    break;
                case MethodProcessingType.FILL_IN_THE_BLANK_PROCESSING:
                    MessageResponse generateFillInTheBlanks = llamaAiService
                            .generateFillInTheBlanks((String) processingTask.getInputParameters().get(0));
                    processingTask.setResult(generateFillInTheBlanks);
                    break;
                case MethodProcessingType.SUMMARY_PROCESSING:
                    MessageResponse generateSummary = llamaAiService
                            .generateSummary((String) processingTask.getInputParameters().get(0));
                    processingTask.setResult(generateSummary);
                    break;
                case MethodProcessingType.TRUE_FALSE_PROCESSING:
                    MessageResponse generateTrueFalseQuestions = llamaAiService
                            .generateTrueFalseQuestions((String) processingTask.getInputParameters().get(0));
                    processingTask.setResult(generateTrueFalseQuestions);
                    break;
                default:
                    log.warn("Unknown message type received from Kafka: {}", processingTask.getMethodProcessingType());
            }

            processingTask.setStatus(TaskStatus.COMPLETED);
            processingTask.setCreatedAt(Instant.now());
            taskManagerService.updateTask(processingTask);
            log.info("Task completed successfully: taskId={}", taskId);
        } catch (Exception e) {
            processingTask.setStatus(TaskStatus.FAILED);
            taskManagerService.updateTask(processingTask);
            log.error("Error processing task {}: {}", taskId, e.getMessage(), e);
        }
    }
}
