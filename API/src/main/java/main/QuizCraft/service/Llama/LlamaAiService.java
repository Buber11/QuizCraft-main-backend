package main.QuizCraft.service.Llama;

import main.QuizCraft.dto.FlashcardDTO;
import main.QuizCraft.dto.QuizDTO;
import main.QuizCraft.response.MessageResponse;

import java.util.List;

public interface LlamaAiService {

     List<QuizDTO> generateQuiz(String prompt);
     List<FlashcardDTO> generateFlashcards(String prompt);
     MessageResponse generateFillInTheBlanks(String prompt);
     MessageResponse generateSummary(String prompt);
     MessageResponse generateTranslateText(String prompt, String targetLanguage);
     MessageResponse generateTrueFalseQuestions(String prompt);
}