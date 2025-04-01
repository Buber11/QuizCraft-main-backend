package main.QuizCraft.service.Auth;

import jakarta.servlet.http.HttpServletRequest;

public interface UserVerificationService {

    void checkAccessForUserToObject(HttpServletRequest request, long ownerId);
}
