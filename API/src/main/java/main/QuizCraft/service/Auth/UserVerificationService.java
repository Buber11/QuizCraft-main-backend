package main.QuizCraft.service.Auth;

import jakarta.servlet.http.HttpServletRequest;

public interface UserVerificationService {

    void verifyOwner(HttpServletRequest request, Long ownerId);
}
