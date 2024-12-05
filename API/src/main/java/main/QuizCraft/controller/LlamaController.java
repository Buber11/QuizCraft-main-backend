package main.QuizCraft.controller;

import lombok.RequiredArgsConstructor;
import main.QuizCraft.response.LlamaResponse;
import main.QuizCraft.response.Response;
import main.QuizCraft.service.LlamaAiService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController()
@RequestMapping("api/v1/ai/generation")
@RequiredArgsConstructor
public class LlamaController {

    private final LlamaAiService llamaAiService;

    @PostMapping("/quiz")
    public <T extends Response> ResponseEntity<T> generateQuiz(
            @RequestBody String promptMessage) {
        final T response = llamaAiService.generateQuiz(promptMessage);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/flashcard")
    public <T extends Response> ResponseEntity<T> generateFlashcard(
            @RequestBody String promptMessage){
        final T response = llamaAiService.generateFlashcards(promptMessage);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/fill-in-the-blanks")
    public <T extends Response> ResponseEntity<T> generateFillInTheBlanks(
            @RequestBody String promptMessage) {
        final T response = llamaAiService.generateFillInTheBlanks(promptMessage);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/summary")
    public <T extends Response> ResponseEntity<T> generateSummary(
            @RequestBody String promptMessage) {
        final T response = llamaAiService.generateSummary(promptMessage);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/translation")
    public <T extends Response> ResponseEntity<T> generateTranslatedText(
            @RequestBody String promptMessage,
            @RequestBody String language){
        final T response = llamaAiService.generateTranslateText(promptMessage, language);
        return ResponseEntity.status(response.getCode()).body(response);
    }

    @PostMapping("/true-or-false-questions")
    public <T extends Response> ResponseEntity<T> generateTrueFalseQuestions(
            @RequestBody String promptMessage){
        final T respone = llamaAiService.generateTrueFalseQuestions(promptMessage);
        return ResponseEntity.status(respone.getCode()).body(respone);
    }



}
