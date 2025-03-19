package main.QuizCraft.exception;

import main.QuizCraft.model.user.User;
import main.QuizCraft.response.AuthResponse;
import main.QuizCraft.response.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalHandlerException {

    @ExceptionHandler(value = BadRequestException.class )
    public AuthResponse handleBadRequest(){
        return new AuthResponse(
                "http://QuizCraft/problems/change-your-username",
                400,
                "Your username isn't unique",
                "The provided username already exists in the system.",
                null
        );
    }

    @ExceptionHandler(value = InvalidCredentialsException.class)
    public AuthResponse handleInvalidCredentials(){
        return new AuthResponse(
                "http://QuizCraft/problems/authenticate",
                401,
                "Invalid credentials",
                "The password provided is incorrect.",
                null
        );
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public AuthResponse handleUserNotFound(){
        return new AuthResponse(
                "http://QuizCraft/problems/authenticate",
                404,
                "User not found",
                "The username provided does not exist in the system.",
                null
        );
    }

}
