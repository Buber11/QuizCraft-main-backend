package main.QuizCraft.service.Llama;

import main.QuizCraft.response.MessageResponse;

public interface LlamaAiService {

     MessageResponse generateQuiz(String prompt);
     MessageResponse generateFlashcards(String prompt);
     MessageResponse generateFillInTheBlanks(String prompt);
     MessageResponse generateSummary(String prompt);
     MessageResponse generateTranslateText(String prompt, String targetLanguage);
     MessageResponse generateTrueFalseQuestions(String prompt);
}