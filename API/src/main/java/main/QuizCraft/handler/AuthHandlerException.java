package main.QuizCraft.handler;

import main.QuizCraft.exception.BadRequestException;
import main.QuizCraft.exception.InvalidCredentialsException;
import main.QuizCraft.exception.UserNotFoundException;
import main.QuizCraft.model.user.User;
import main.QuizCraft.response.FailureResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthHandlerException {

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
                        "The username provided does not exist in the system.",
                        null
                ));
    }


}
