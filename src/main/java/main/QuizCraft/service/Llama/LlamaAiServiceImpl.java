package main.QuizCraft.service.Llama;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import main.QuizCraft.dto.FlashcardDTO;
import main.QuizCraft.dto.QuizDTO;
import main.QuizCraft.exception.AiResponseException;
import main.QuizCraft.response.MessageResponse;
import main.QuizCraft.service.flashcard.FlashcardAssembler;
import main.QuizCraft.service.quiz.QuizAssembler;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class LlamaAiServiceImpl implements LlamaAiService {

    private final OllamaChatModel ollamaChatModel;
    private final BeanFactory beanFactory;
    private final FlashcardAssembler flashcardAssembler;
    private final QuizAssembler quizAssembler;
    private final ObjectMapper objectMapper = new ObjectMapper();


    private static final String QUIZ_TEMPLATE =
            "Read the following text and create a Quiz based on it. The Quiz should consist of 4 questions. " +
            "Each question should have 4 answer options, and only one of them should be correct. " +
            "Format the output as valid JSON as follows:\n\n" +
            "{\n" +
            "  \"quiz\": [\n" +
            "    {\n" +
            "      \"question\": \"[Your question here]\",\n" +
            "      \"options\": [\"Option 1\", \"Option 2\", \"Option 3\", \"Option 4\"],\n" +
            "      \"correctAnswer\": \"[Correct option] (e.g., Option 1)\"\n" +
            "    },\n" +
            "    ...\n" +
            "  ]\n" +
            "}\n\n" +
            "Here is the text to analyze:\n%s";

    private static final String FLASHCARD_TEMPLATE =
            "Read the following text and create flashcards based on it. Each flashcard should have a question on one side " +
            "and an answer on the other. Format the output as valid JSON as follows:\n\n" +
            "{\n" +
            "  \"flashcards\": [\n" +
            "    {\n" +
            "      \"front\": \"[Your question here]\",\n" +
            "      \"back\": \"[Answer here]\"\n" +
            "    },\n" +
            "    ...\n" +
            "  ]\n" +
            "}\n\n" +
            "Here is the text to analyze:\n%s";

    private static final String FILL_IN_THE_BLANKS_TEMPLATE =
            "Read the following text and create a fill-in-the-blank exercise. Remove key words from the text " +
            "and replace them with blanks (____). Provide the correct answers separately. Format the output as follows:\n\n" +
            "Fill-in-the-Blank Text:\n" +
            "[Your modified text with blanks here]\n\n" +
            "Answers:\n" +
            "1. [Correct word for blank 1]\n" +
            "2. [Correct word for blank 2]\n" +
            "3. [Correct word for blank 3]\n\n" +
            "Here is the text to analyze:\n%s";

    private static final String SUMMARY_TEMPLATE =
            "Read the following text and generate a concise summary highlighting the main points. " +
            "The summary should be no longer than 3-4 sentences. Format the output as follows:\n\n" +
            "Summary:\n" +
            "[Your summary here]\n\n" +
            "Here is the text to analyze:\n%s";

    private static final String TRUE_FALSE_TEMPLATE =
            "Read the following text and create 5 True/False questions based on it. Format the output as follows:\n\n" +
            "True or False Question 1:\n" +
            "[Your statement here] (True/False)\n\n" +
            "True or False Question 2:\n" +
            "[Your statement here] (True/False)\n\n" +
            "Here is the text to analyze:\n%s";


    private String formatContext(List<?> context) {
        if (context == null || context.isEmpty()) {
            return "No context provided.";
        }

        StringBuilder sb = new StringBuilder();
        for (Object item : context) {
            if (item instanceof Map) {
                Map<?, ?> map = (Map<?, ?>) item;
                if (map.containsKey("sentence") && map.containsKey("score")) {
                    sb.append("- ").append(map.get("sentence"))
                      .append(" (relevance score: ").append(map.get("score")).append(")\n");
                } else {
                    sb.append(map).append("\n");
                }
            } else {
                sb.append(item).append("\n");
            }
        }
        return sb.toString();
    }

    private ChatResponse callAiModel(String templatePrompt, List<?> context, String operationType) {
        String contextStr = formatContext(context);
        String formattedPrompt = String.format(templatePrompt, contextStr);

        log.debug("Generating {} with context size: {}", operationType, context.size());

        try {
            return ollamaChatModel.call(
                new Prompt(formattedPrompt, beanFactory.getBean("llama3_2", OllamaOptions.class))
            );
        } catch (Exception e) {
            log.error("Error calling AI model for {}: {}", operationType, e.getMessage());
            throw new AiResponseException(
                "Error generating " + operationType,
                "Error calling AI model: " + e.getMessage()
            );
        }
    }

    private String extractJsonFromResponse(String response) {
        int jsonStart = response.indexOf("{");
        int jsonEnd = response.lastIndexOf("}") + 1;

        if (jsonStart == -1 || jsonEnd <= jsonStart) {
            log.warn("No valid JSON found in response: {}", response);
            throw new AiResponseException("Invalid response format", "No valid JSON found in response");
        }

        return response.substring(jsonStart, jsonEnd);
    }

    @Override
    public List<QuizDTO> generateQuiz(List<?> context) {
        try {
            ChatResponse response = callAiModel(QUIZ_TEMPLATE, context, "quiz");
            String jsonResponse = extractJsonFromResponse(response.getResult().toString());

            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            List<QuizDTO> quizQuestions = new ArrayList<>();

            if (rootNode.has("quiz") && rootNode.get("quiz").isArray()) {
                rootNode.get("quiz").forEach(node -> {
                    try {
                        String question = node.get("question").asText();
                        List<String> options = new ArrayList<>();
                        node.get("options").forEach(option -> options.add(option.asText()));
                        String correctAnswer = node.get("correctAnswer").asText();

                        if (options.size() == 4) {
                            QuizDTO quizDTO = new QuizDTO(
                                null,
                                question,
                                correctAnswer,
                                options.get(0).equals(correctAnswer) ? options.get(1) : options.get(0),
                                options.get(1).equals(correctAnswer) ? options.get(2) : options.get(1),
                                options.get(2).equals(correctAnswer) ? options.get(3) : options.get(2)
                            );
                            quizQuestions.add(quizAssembler.addCreateLink(quizDTO));
                        }
                    } catch (Exception e) {
                        log.warn("Error parsing quiz question: {}", e.getMessage());
                    }
                });
            }

            log.info("Generated {} quiz questions", quizQuestions.size());
            return quizQuestions;

        } catch (JsonProcessingException e) {
            log.error("JSON parsing error: {}", e.getMessage());
            throw new AiResponseException("Error parsing JSON response", e.getMessage());
        } catch (AiResponseException e) {
            throw e;
        } catch (Exception e) {
            log.error("Unexpected error generating quiz: {}", e.getMessage(), e);
            throw new AiResponseException("Error generating Quiz", e.getMessage());
        }
    }

    @Override
    public List<FlashcardDTO> generateFlashcards(List<?> context) {
        try {
            ChatResponse response = callAiModel(FLASHCARD_TEMPLATE, context, "flashcards");
            String jsonResponse = extractJsonFromResponse(response.getResult().toString());

            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            List<FlashcardDTO> flashcardDTOS = new ArrayList<>();

            if (rootNode.has("flashcards") && rootNode.get("flashcards").isArray()) {
                rootNode.get("flashcards").forEach(node -> {
                    try {
                        String front = node.get("front").asText();
                        String back = node.get("back").asText();
                        flashcardDTOS.add(flashcardAssembler.toModel(front, back));
                    } catch (Exception e) {
                        log.warn("Error parsing flashcard: {}", e.getMessage());
                    }
                });
            }

            log.info("Generated {} flashcards", flashcardDTOS.size());
            return flashcardDTOS;

        } catch (Exception e) {
            log.error("Error generating flashcards: {}", e.getMessage(), e);
            throw new AiResponseException("Error generating flashcards", e.getMessage());
        }
    }

    @Override
    public MessageResponse generateFillInTheBlanks(List<?> context) {
        try {
            ChatResponse response = callAiModel(FILL_IN_THE_BLANKS_TEMPLATE,
                    context,
                    "fill-in-the-blanks");
            log.info("Generated fill-in-the-blanks exercise");
            return new MessageResponse(response.getResult().toString());
        } catch (Exception e) {
            log.error("Error generating fill-in-the-blanks: {}", e.getMessage(), e);
            throw new AiResponseException("Error generating fill-in-the-blank exercise", e.getMessage());
        }
    }

    @Override
    public MessageResponse generateSummary(List<?> context) {
        try {
            ChatResponse response = callAiModel(SUMMARY_TEMPLATE, context, "summary");
            log.info("Generated summary");
            return new MessageResponse(response.getResult().toString());
        } catch (Exception e) {
            log.error("Error generating summary: {}", e.getMessage(), e);
            throw new AiResponseException("Error generating summary", e.getMessage());
        }
    }

    @Override
    public MessageResponse generateTrueFalseQuestions(List<?> context) {
        try {
            ChatResponse response = callAiModel(TRUE_FALSE_TEMPLATE,
                    context,
                    "true-false questions");
            log.info("Generated true/false questions");
            return new MessageResponse(response.getResult().toString());
        } catch (Exception e) {
            log.error("Error generating true/false questions: {}", e.getMessage(), e);
            throw new AiResponseException("Error generating True/False questions", e.getMessage());
        }
    }
}