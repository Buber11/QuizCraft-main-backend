package main.QuizCraft.handler;

import main.QuizCraft.exception.AiResponseException;
import main.QuizCraft.response.FailureResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.net.http.HttpResponse;

@RestControllerAdvice
public class AiHandlerException {

    @ExceptionHandler(value = AiResponseException.class)
    public ResponseEntity<FailureResponse> handleAiResponseException(AiResponseException responseException){
        return ResponseEntity
                .status(500)
                .body( new FailureResponse(
                        "http://QuizCraft/problems/lack-of-ai-connection",
                        500,
                        responseException.getTitle(),
                        responseException.getDetails(),
                        null
                ) );
    }

}
