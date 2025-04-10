package main.QuizCraft.handler;

import main.QuizCraft.exception.ResourceNotFoundException;
import main.QuizCraft.response.FailureResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;


@RestControllerAdvice
public class DeckHandlerException {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<FailureResponse> handleResourceNotFound(ResourceNotFoundException e){
        return ResponseEntity
                .status(404)
                .body( FailureResponse.builder()
                        .title(e.getMessage())
                        .code(404)
                        .instance(e.getInstanceName() + "?id=" + e.getId())
                        .status("http://QuizCraft/problems/resourceNotFound/deck")
                        .detail("You have to use diffrent id")
                        .build()
                );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<FailureResponse> handleAccessDenied(AccessDeniedException e){
        return ResponseEntity
                .status(404)
                .body( FailureResponse.builder()
                        .title(e.getMessage())
                        .code(404)
                        .instance(null)
                        .status("http://QuizCraft/problems/resourceNotFound/deck")
                        .detail("You have to use diffrent id")
                        .build()
                );
    }


}
