package main.QuizCraft.controller;

import lombok.RequiredArgsConstructor;
import main.QuizCraft.response.MessageResponse;
import main.QuizCraft.service.Llama.LlamaAiService;
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

    @PostMapping("/Quiz")
    public  ResponseEntity generateQuiz(
            @RequestBody String promptMessage) {
        final MessageResponse messageResponse = llamaAiService.generateQuiz(promptMessage);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping("/flashcard")
    public  ResponseEntity generateFlashcard(
            @RequestBody String promptMessage){
        final MessageResponse messageResponse = llamaAiService.generateFlashcards(promptMessage);
        return ResponseEntity.status(HttpStatus.OK).body(messageResponse);
    }

    @PostMapping("/fill-in-the-blanks")
    public ResponseEntity generateFillInTheBlanks(
            @RequestBody String promptMessage) {
        final MessageResponse response = llamaAiService.generateFillInTheBlanks(promptMessage);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/summary")
    public ResponseEntity generateSummary(
            @RequestBody String promptMessage) {
        final MessageResponse response = llamaAiService.generateSummary(promptMessage);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/translation")
    public ResponseEntity generateTranslatedText(
            @RequestBody String promptMessage,
            @RequestBody String language){
        final MessageResponse response = llamaAiService.generateTranslateText(promptMessage, language);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @PostMapping("/true-or-false-questions")
    public ResponseEntity generateTrueFalseQuestions(
            @RequestBody String promptMessage){
        final MessageResponse respone = llamaAiService.generateTrueFalseQuestions(promptMessage);
        return ResponseEntity.status(HttpStatus.OK).body(respone);
    }



}
