package main.QuizCraft.service.Auth;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import main.QuizCraft.request.AuthRequest;

public interface AuthService {

    void authenticate(AuthRequest authRequst,
                      HttpServletRequest httpServletRequest,
                      HttpServletResponse response);
    void register(AuthRequest authRequst);
    void renewCookie(HttpServletRequest request,
                     HttpServletResponse response);
    void logout(HttpServletRequest request);

}
