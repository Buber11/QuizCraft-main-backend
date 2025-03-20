package main.QuizCraft.exception;

import main.QuizCraft.response.MessageResponse;
import main.QuizCraft.response.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AiHandlerException {

    @ExceptionHandler(value = AiResponseException.class)
    public Response<String> handleAiResponseException(AiResponseException responseException){
        return new MessageResponse(
                "http://QuizCraft/problems/lack-of-ai-connection",
                500,
                responseException.getTitle(),
                responseException.getDetails(),
                null
        );
    }

}
