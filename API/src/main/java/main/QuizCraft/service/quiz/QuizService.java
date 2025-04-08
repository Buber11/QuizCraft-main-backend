package main.QuizCraft.service.quiz;

import jakarta.servlet.http.HttpServletRequest;

import main.QuizCraft.dto.QuizDTO;
import main.QuizCraft.request.QuizRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuizService {
    Page<QuizDTO> loadQuizzes(long deckID, Pageable pageable, HttpServletRequest request);
    QuizDTO loadQuizById(long deckId, HttpServletRequest request);
    void deleteQuiz(long id, HttpServletRequest request);
    QuizDTO updateQuiz(long id, QuizRequest quizRequest, HttpServletRequest request);
    void saveQuiz(QuizRequest quizRequest, HttpServletRequest request);

}