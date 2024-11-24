package main.QuizCraft.service;

import main.QuizCraft.response.LlamaResponse;
import main.QuizCraft.response.Response;

public interface LlamaAiService {

    <T extends  Response> T generateQuiz(String prompt);
    <T extends  Response> T generateFlashcards(String prompt);
    <T extends  Response> T generateFillInTheBlanks(String prompt);
    <T extends  Response> T generateSummary(String prompt);

}