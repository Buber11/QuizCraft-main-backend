package main.QuizCraft.model.deck.dto;

public record QuizDTO(
        String question,
        String correctAnswer,
        String badAnswer1,
        String badAnswer2,
        String badAnswer3
) {
}
