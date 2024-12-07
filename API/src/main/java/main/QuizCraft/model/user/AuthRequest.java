package main.QuizCraft.model.user;

public record AuthRequest(
        String username,
        String password
) {
}
