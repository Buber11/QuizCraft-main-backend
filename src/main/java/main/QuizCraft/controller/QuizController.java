package main.QuizCraft.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.request.QuizRequest;
import main.QuizCraft.service.quiz.QuizService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import main.QuizCraft.dto.QuizDTO;
import main.QuizCraft.service.quiz.QuizServiceImpl;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deck")
@RequiredArgsConstructor
public class QuizController {

    private final QuizService quizService;

    @GetMapping("/{deckID}/quiz")
    public ResponseEntity<Page<QuizDTO>> getAllQuizzes(
            @PathVariable(value = "deckID") long deckID,
            @PageableDefault(size = 10,
                    sort = "question",
                    direction = Sort.Direction.DESC)
            Pageable pageable,
            HttpServletRequest httpServletRequest) {

        Page<QuizDTO> quizDTOS = quizService.loadQuizzes(deckID, pageable, httpServletRequest);
        return ResponseEntity.ok(quizDTOS);
    }

    @GetMapping("quiz/{id}")
    public ResponseEntity<QuizDTO> getQuizById(
            @PathVariable("id") Long quizId,
            HttpServletRequest httpServletRequest) {
        QuizDTO quizDTO = quizService.loadQuizById(quizId, httpServletRequest);
        return ResponseEntity.ok(quizDTO);
    }

    @PostMapping("/quiz")
    public ResponseEntity createQuiz(
            @RequestBody QuizRequest quizRequest,
            HttpServletRequest httpServletRequest) {
        quizService.saveQuiz(quizRequest, httpServletRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/quiz/{id}")
    public ResponseEntity<QuizDTO> updateQuiz(
            @PathVariable Long id,
            @RequestBody QuizRequest quizRequest,
            HttpServletRequest httpServletRequest) {
        QuizDTO updatedQuiz = quizService.updateQuiz(id, quizRequest, httpServletRequest);
        return ResponseEntity.ok(updatedQuiz);
    }

    @DeleteMapping("/quiz/{id}")
    public ResponseEntity deleteQuiz(
            @PathVariable Long id,
            HttpServletRequest httpServletRequest) {
        quizService.deleteQuiz(id, httpServletRequest);
        return ResponseEntity.noContent().build();
    }
}
