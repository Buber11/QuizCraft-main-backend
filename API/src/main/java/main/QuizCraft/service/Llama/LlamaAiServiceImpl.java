package main.QuizCraft.service.Llama;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.dto.FlashcardDTO;
import main.QuizCraft.dto.QuizDTO;
import main.QuizCraft.exception.AiResponseException;
import main.QuizCraft.model.deck.Quiz;
import main.QuizCraft.response.MessageResponse;
import main.QuizCraft.service.flashcard.FlashcardAssembler;
import main.QuizCraft.service.quiz.QuizAssembler;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.ai.ollama.api.OllamaModel;
import org.springframework.ai.ollama.api.OllamaOptions;
import org.springframework.beans.factory.BeanFactory;
import org.springframework.stereotype.Service;
import java.util.regex.*;
import java.util.*;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class LlamaAiServiceImpl implements LlamaAiService{

    private final OllamaChatModel ollamaChatModel;
    private final BeanFactory beanFactory;
    private final FlashcardAssembler flashcardAssembler;
    private final QuizAssembler quizAssembler;

    @Override
    public List<QuizDTO> generateQuiz(String prompt) {
        try {
            String formattedPrompt = String.format(
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
                            "Here is the text for the Quiz:\n%s",
                    prompt
            );

            final ChatResponse llamaMessage = ollamaChatModel.call(
                    new Prompt(formattedPrompt, beanFactory.getBean("deepseek-r1", OllamaOptions.class))
            );

            String jsonResponse = llamaMessage.getResult().toString();
            int jsonStart = jsonResponse.indexOf("{");
            int jsonEnd = jsonResponse.lastIndexOf("}") + 1;
            jsonResponse = jsonResponse.substring(jsonStart, jsonEnd);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            List<QuizDTO> quizQuestions = new ArrayList<>();

            rootNode.get("quiz").forEach(node -> {
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
            });
            return quizQuestions;
        } catch (Exception e) {
            throw new AiResponseException("Error generating Quiz", "Error generating Quiz: " + e.getMessage());
        }
    }



    @Override
    public List<FlashcardDTO> generateFlashcards(String prompt) {
        try {
            String formattedPrompt = String.format(
                    "Read the following text and create flashcards based on it. Each flashcard should have a question on one side and an answer on the other. Format the output as valid JSON as follows:\n\n" +
                            "{\n" +
                            "  \"flashcards\": [\n" +
                            "    {\n" +
                            "      \"front\": \"[Your question here]\",\n" +
                            "      \"back\": \"[Answer here]\"\n" +
                            "    },\n" +
                            "    ...\n" +
                            "  ]\n" +
                            "}\n\n" +
                            "Here is the text for the flashcards:\n%s",
                    prompt
            );
            final ChatResponse llamaMessage = ollamaChatModel.call(
                    new Prompt(formattedPrompt, beanFactory.getBean("deepseek-r1", OllamaOptions.class))
            );

            String jsonResponse = llamaMessage.getResult().toString();
            int jsonStart = jsonResponse.indexOf("{");
            int jsonEnd = jsonResponse.lastIndexOf("}") + 1;
            jsonResponse = jsonResponse.substring(jsonStart, jsonEnd);

            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            List<FlashcardDTO> flashcardDTOS = new ArrayList<>();
            rootNode.get("flashcards").forEach(node -> {
                String front = node.get("front").asText();
                String back = node.get("back").asText();
                flashcardDTOS.add(flashcardAssembler.toModel(front, back));
            });

            return flashcardDTOS;
        } catch (Exception e) {
            throw new AiResponseException("Error generating flashcards", "Error generating flashcards: " + e.getMessage());
        }
    }

    @Override
    public MessageResponse generateFillInTheBlanks(String prompt) {
        try {
            String formattedPrompt = String.format(
                    "Read the following text and create a fill-in-the-blank exercise. Remove key words from the text and replace them with blanks (____). Provide the correct answers separately. Format the output as follows:\n\n" +
                            "    Fill-in-the-Blank Text:\n" +
                            "    [Your modified text with blanks here]\n\n" +
                            "    Answers:\n" +
                            "    1. [Correct word for blank 1]\n" +
                            "    2. [Correct word for blank 2]\n" +
                            "    3. [Correct word for blank 3]\n\n" +
                            "Here is the text to process:\n%s",
                    prompt
            );
            final ChatResponse llamaMessage = ollamaChatModel.call(
                    new Prompt(formattedPrompt, beanFactory.getBean("deepseek-r1", OllamaOptions.class))
            );
            System.out.println(llamaMessage.getResults().toString());
            return new MessageResponse(llamaMessage.toString());
        } catch (Exception e) {
            throw new AiResponseException("Error generating fill-in-the-blank exercise", "Error generating fill-in-the-blank exercise: " + e.getMessage());
        }
    }

    @Override
    public MessageResponse generateSummary(String prompt) {
        try {
            String formattedPrompt = String.format(
                    "Read the following text and generate a concise summary highlighting the main points. The summary should be no longer than 3-4 sentences. Format the output as follows:\n\n" +
                            "    Summary:\n" +
                            "    [Your summary here]\n\n" +
                            "Here is the text to summarize:\n%s",
                    prompt
            );
            final ChatResponse llamaMessage = ollamaChatModel.call(
                    new Prompt(formattedPrompt, beanFactory.getBean("deepseek-r1", OllamaOptions.class))
            );
            System.out.println(llamaMessage.getResults().toString());
            return new MessageResponse(llamaMessage.toString());
        } catch (Exception e) {
            throw new AiResponseException("Error generating summary", "Error generating summary: " + e.getMessage());
        }
    }

    @Override
    public MessageResponse generateTranslateText(String prompt, String targetLanguage) {
        try {
            String formattedPrompt = String.format(
                    "Translate the following text into %s. Format the output as follows:\n\n" +
                            "    Translated Text:\n" +
                            "    [Your translated text here]\n\n" +
                            "Here is the text to translate:\n%s",
                    targetLanguage, prompt
            );
            final ChatResponse llamaMessage = ollamaChatModel.call(
                    new Prompt(formattedPrompt, beanFactory.getBean("deepseek-r1", OllamaOptions.class))
            );
            System.out.println(llamaMessage.getResults().toString());
            return new MessageResponse(llamaMessage.toString());
        } catch (Exception e) {
            throw new AiResponseException("Error translating text", "Error translating text: " + e.getMessage());
        }
    }

    @Override
    public MessageResponse generateTrueFalseQuestions(String prompt) {
        try {
            String formattedPrompt = String.format(
                    "Read the following text and create 5 True/False questions based on it. Format the output as follows:\n\n" +
                            "    True or False Question:\n" +
                            "    [Your statement here] (if statement is true then write True/ if statement is false then write False)\n\n" +
                            "Here is the text to process:\n%s",
                    prompt
            );
            final ChatResponse llamaMessage = ollamaChatModel.call(
                    new Prompt(formattedPrompt, beanFactory.getBean("deepseek-r1", OllamaOptions.class))
            );
            System.out.println(llamaMessage.getResults().toString());
            return new MessageResponse(llamaMessage.toString());
        } catch (Exception e) {
            throw new AiResponseException("Error generating True/False questions", "Error generating True/False questions: " + e.getMessage());
        }
    }

}
