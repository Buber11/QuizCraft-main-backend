package main.QuizCraft.service.quiz;

import main.QuizCraft.dto.QuizDTO;
import main.QuizCraft.model.deck.Quiz;
import main.QuizCraft.projection.QuizProjection;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.stereotype.Component;
import main.QuizCraft.controller.QuizController;

@Component
public class QuizAssembler {

    public QuizDTO addCreateLink(QuizDTO quizDTO) {
        quizDTO.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(QuizController.class)
                        .createQuiz(null, null))
                .withRel("create"));
        return quizDTO;
    }

    public QuizDTO toDTO(Quiz quiz) {
        QuizDTO quizDTO = new QuizDTO(
                quiz.getId(),
                quiz.getQuestion(),
                quiz.getCorrectAnswer(),
                quiz.getBadAnswer1(),
                quiz.getBadAnswer2(),
                quiz.getBadAnswer3()
        );
        addLinks(quizDTO, quiz.getId(), quiz.getDeck().getId());
        return quizDTO;
    }

    public QuizDTO toDTO(QuizProjection projection) {
        QuizDTO quizDTO = new QuizDTO(
                projection.getId(),
                projection.getQuestion(),
                projection.getCorrectAnswer(),
                projection.getBadAnswer1(),
                projection.getBadAnswer2(),
                projection.getBadAnswer3()
        );
        addLinks(quizDTO, projection.getId(), null);
        return quizDTO;
    }

    private void addLinks(QuizDTO quizDTO, Long quizId, Long deckId) {
        quizDTO.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(QuizController.class)
                        .getAllQuizzes(deckId, null, null))
                .withRel("quizzes"));

        quizDTO.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(QuizController.class)
                        .getQuizById(quizId, null))
                .withSelfRel());

        quizDTO.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(QuizController.class)
                        .createQuiz(null, null))
                .withRel("create"));

        quizDTO.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(QuizController.class)
                        .updateQuiz(quizId, null, null))
                .withRel("update"));

        quizDTO.add(WebMvcLinkBuilder.linkTo(
                WebMvcLinkBuilder.methodOn(QuizController.class)
                        .deleteQuiz(quizId, null))
                .withRel("delete"));
    }
}