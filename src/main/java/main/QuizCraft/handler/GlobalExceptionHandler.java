package main.QuizCraft.handler;

import main.QuizCraft.exception.*;
import main.QuizCraft.response.FailureResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(value = BadRequestException.class )
    public ResponseEntity<FailureResponse> handleBadRequestException(){
        return ResponseEntity
                .status(400)
                .body(new FailureResponse(
                        "http://QuizCraft/problems/change-your-username",
                        400,
                        "Your username isn't unique",
                        "The provided username already exists in the system.",
                        null
                ));
    }

    @ExceptionHandler(value = InvalidCredentialsException.class)
    public ResponseEntity<FailureResponse> handleInvalidCredentialsException(){
        return ResponseEntity
                .status(401)
                .body(new FailureResponse(
                        "http://QuizCraft/problems/authenticate",
                        401,
                        "Invalid credentials",
                        "The password provided is incorrect.",
                        null
                ));
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public ResponseEntity<FailureResponse> handleUserNotFoundException(){
        return ResponseEntity
                .status(404)
                .body(new FailureResponse(
                        "http://QuizCraft/problems/authenticate",
                        404,
                        "User not found",
                        "The username or id provided does not exist in the system.",
                        null
                ));
    }

    @ExceptionHandler(value = AiResponseException.class)
    public ResponseEntity<FailureResponse> handleAiResponseException(AiResponseException responseException) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new FailureResponse(
                        "http://QuizCraft/problems/lack-of-ai-connection",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        responseException.getTitle(),
                        responseException.getDetails(),
                        null
                ));
    }

    @ExceptionHandler(value = IllegalArgumentException.class)
    public ResponseEntity<FailureResponse> handleIllegalArgumentException(IllegalArgumentException exception) {
        return ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(new FailureResponse(
                        "http://QuizCraft/problems/invalid-argument",
                        HttpStatus.BAD_REQUEST.value(),
                        "Invalid Argument",
                        exception.getMessage(),
                        null
                ));
    }

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

    @ExceptionHandler(value = ProcessingTaskException.class)
    public ResponseEntity<FailureResponse> handleProcessingTaskException(ProcessingTaskException e) {
        return ResponseEntity
                .status(404)
                .body( FailureResponse.builder()
                        .title("Task processing error")
                        .code(404)
                        .instance("task")
                        .status("http://QuizCraft/problems/task-processing")
                        .detail(e.getMessage())
                        .build()
                );
    }

    @ExceptionHandler(value = Exception.class)
    public ResponseEntity<FailureResponse> handleGenericException(Exception exception) {
        return ResponseEntity
                .status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new FailureResponse(
                        "http://QuizCraft/problems/internal-server-error",
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        "Internal Server Error",
                        exception.getMessage(),
                        null
                ));
    }
}