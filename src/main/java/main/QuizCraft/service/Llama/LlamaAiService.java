package main.QuizCraft.service.Llama;

import main.QuizCraft.dto.FlashcardDTO;
import main.QuizCraft.dto.QuizDTO;
import main.QuizCraft.response.MessageResponse;

import java.util.List;

public interface LlamaAiService {

     List<QuizDTO> generateQuiz(List<?> contex);
     List<FlashcardDTO> generateFlashcards(List<?> contex);
     MessageResponse generateFillInTheBlanks(List<?> contex);
     MessageResponse generateSummary(List<?> contex);
     MessageResponse generateTrueFalseQuestions(List<?> contex);
}