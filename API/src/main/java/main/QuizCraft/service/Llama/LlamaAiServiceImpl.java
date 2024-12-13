package main.QuizCraft.service.Llama;

import lombok.RequiredArgsConstructor;
import main.QuizCraft.response.MessageResponse;
import org.springframework.ai.ollama.OllamaChatModel;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LlamaAiServiceImpl implements LlamaAiService {

    private final OllamaChatModel ollamaChatModel;

    @Override
    public MessageResponse generateQuiz(String prompt) {
        try {
            String formattedPrompt = String.format("Read the following text and create a quiz based on it. The quiz should consist of 4 questions. Each question should have 4 answer options, and only one of them should be correct. Highlight the correct answer. Format the output as follows:\n" +
                    "\n" +
                    "    Question: [Your question here]\n" +
                    "        a) [Option 1]\n" +
                    "        b) [Option 2]\n" +
                    "        c) [Option 3]\n" +
                    "        d) [Option 4]\n" +
                    "        Correct Answer: [Correct option]\n" +
                    "\n" +
                    "Here is the text for the quiz:\n" +
                    "%s", prompt);
            final String llamaMessage = ollamaChatModel.call(formattedPrompt);
            return new MessageResponse(llamaMessage);
        } catch (Exception e) {
            return new MessageResponse(
                    "http://QuizCraft/problems/lack-of-ai-connection",
                    500,
                    "Error generating quiz",
                    "Error generating quiz: " + e.getMessage(),
                    null
            );
        }
    }

    @Override
    public MessageResponse generateFlashcards(String prompt) {
        try {
            String formattedPrompt = String.format("Read the following text and create flashcards based on it. Each flashcard should have a question on one side and an answer on the other. Format the output as follows:\n" +
                    "\n" +
                    "    Flashcard:\n" +
                    "        Question: [Your question here]\n" +
                    "        Answer: [Answer here]\n" +
                    "\n" +
                    "Here is the text for the flashcards:\n" +
                    "%s", prompt);
            final String llamaMessage = ollamaChatModel.call(formattedPrompt);
            return new MessageResponse(llamaMessage);
        } catch (Exception e) {
            return new MessageResponse(
                    "http://QuizCraft/problems/lack-of-ai-connection",
                    500,
                    "Error generating flashcards",
                    "Error generating flashcards: " + e.getMessage(),
                    null
            );
        }
    }

    @Override
    public MessageResponse generateFillInTheBlanks(String prompt) {
        try {
            String formattedPrompt = String.format(
                    "Read the following text and create a fill-in-the-blank exercise. Remove key words from the text and replace them with blanks (____). Provide the correct answers separately. Format the output as follows:\n" +
                            "\n" +
                            "    Fill-in-the-Blank Text:\n" +
                            "    [Your modified text with blanks here]\n" +
                            "    \n" +
                            "    Answers:\n" +
                            "    1. [Correct word for blank 1]\n" +
                            "    2. [Correct word for blank 2]\n" +
                            "    3. [Correct word for blank 3]\n" +
                            "\n" +
                            "Here is the text to process:\n%s",
                    prompt
            );
            final String llamaMessage = ollamaChatModel.call(formattedPrompt);
            return new MessageResponse(llamaMessage);
        } catch (Exception e) {
            return new MessageResponse(
                    "http://QuizCraft/problems/lack-of-ai-connection",
                    500,
                    "Error generating fill-in-the-blank exercise",
                    "Error generating fill-in-the-blank exercise: " + e.getMessage(),
                    null
            );
        }
    }

    @Override
    public MessageResponse generateSummary(String prompt) {
        try {
            String formattedPrompt = String.format(
                    "Read the following text and generate a concise summary highlighting the main points. The summary should be no longer than 3-4 sentences. Format the output as follows:\n" +
                            "\n" +
                            "    Summary:\n" +
                            "    [Your summary here]\n" +
                            "\n" +
                            "Here is the text to summarize:\n%s",
                    prompt
            );
            final String llamaMessage = ollamaChatModel.call(formattedPrompt);
            return new MessageResponse(llamaMessage);
        } catch (Exception e) {
            return new MessageResponse(
                    "http://QuizCraft/problems/lack-of-ai-connection",
                    500,
                    "Error generating summary",
                    "Error generating summary: " + e.getMessage(),
                    null
            );
        }
    }

    @Override
    public MessageResponse generateTranslateText(String prompt, String targetLanguage) {
        try {
            String formattedPrompt = String.format(
                    "Translate the following text into %s. Format the output as follows:\n" +
                            "\n" +
                            "    Translated Text:\n" +
                            "    [Your translated text here]\n" +
                            "\n" +
                            "Here is the text to translate:\n%s",
                    targetLanguage, prompt
            );
            final String llamaMessage = ollamaChatModel.call(formattedPrompt);
            return new MessageResponse(llamaMessage);
        } catch (Exception e) {
            return new MessageResponse(
                    "http://QuizCraft/problems/lack-of-ai-connection",
                    500,
                    "Error translating text",
                    "Error translating text: " + e.getMessage(),
                    null
            );
        }
    }

    @Override
    public MessageResponse generateTrueFalseQuestions(String prompt) {
        try {
            String formattedPrompt = String.format(
                    "Read the following text and create 5 True/False questions based on it. Format the output as follows:\n" +
                            "\n" +
                            "    True/False Question:\n" +
                            "    [Your statement here] (True/False)\n" +
                            "\n" +
                            "Here is the text to process:\n%s",
                    prompt
            );
            final String llamaMessage = ollamaChatModel.call(formattedPrompt);
            return new MessageResponse(llamaMessage);
        } catch (Exception e) {
            return new MessageResponse(
                    "http://QuizCraft/problems/lack-of-ai-connection",
                    500,
                    "Error generating True/False questions",
                    "Error generating True/False questions: " + e.getMessage(),
                    null
            );
        }
    }


}
