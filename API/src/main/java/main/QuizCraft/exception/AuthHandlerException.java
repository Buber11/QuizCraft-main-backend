package main.QuizCraft.exception;

import main.QuizCraft.model.user.User;
import main.QuizCraft.response.AuthResponse;
import main.QuizCraft.response.Response;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class AuthHandlerException {

    @ExceptionHandler(value = BadRequestException.class )
    public Response<User> handleBadRequestException(){
        return new AuthResponse(
                "http://QuizCraft/problems/change-your-username",
                400,
                "Your username isn't unique",
                "The provided username already exists in the system.",
                null
        );
    }

    @ExceptionHandler(value = InvalidCredentialsException.class)
    public Response<User> handleInvalidCredentialsException(){
        return new AuthResponse(
                "http://QuizCraft/problems/authenticate",
                401,
                "Invalid credentials",
                "The password provided is incorrect.",
                null
        );
    }

    @ExceptionHandler(value = UserNotFoundException.class)
    public Response<User> handleUserNotFoundException(){
        return new AuthResponse(
                "http://QuizCraft/problems/authenticate",
                404,
                "User not found",
                "The username provided does not exist in the system.",
                null
        );
    }


}
