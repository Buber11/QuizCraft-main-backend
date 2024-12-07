package main.QuizCraft.service.Auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.QuizCraft.model.user.AuthRequest;
import main.QuizCraft.response.Response;

public interface AuthService {

    <T extends Response> T authenticate(AuthRequest authRequst, HttpServletResponse response);
    <T extends Response> T register(AuthRequest authRequst);
    <T extends Response> T renewCookie(HttpServletRequest request, HttpServletResponse response);

}
