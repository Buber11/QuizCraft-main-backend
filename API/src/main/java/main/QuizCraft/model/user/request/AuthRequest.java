package main.QuizCraft.model.user.request;

public record AuthRequest(
        String username,
        String password
) {
}
