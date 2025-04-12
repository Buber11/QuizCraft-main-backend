package main.QuizCraft.service.quiz;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.dto.QuizDTO;
import main.QuizCraft.exception.ResourceNotFoundException;
import main.QuizCraft.model.Quiz;
import main.QuizCraft.repository.QuizRepository;
import main.QuizCraft.request.QuizRequest;
import main.QuizCraft.service.Auth.UserVerificationService;
import main.QuizCraft.service.deck.DeckAccessService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class QuizServiceImpl implements QuizService {

    private final QuizRepository quizRepository;
    private final UserVerificationService userVerificationService;
    private final DeckAccessService deckAccessService;
    private final QuizAssembler assembler;

    private final Logger LOGGER = LoggerFactory.getLogger(QuizServiceImpl.class);

    @Override
    @Transactional(readOnly = true)
    public Page<QuizDTO> loadQuizzes(long deckID, Pageable pageable, HttpServletRequest request) {
        LOGGER.info("Loading quizzes for deck ID: {}", deckID);
        Long ownerId = deckAccessService.getOwnerId(deckID);
        userVerificationService.verifyOwner(request, ownerId);
        return quizRepository.readDTO(deckID, pageable)
                .map(assembler::toDTO);

    }

    @Override
    @Transactional(readOnly = true)
    public QuizDTO loadQuizById(long quizId, HttpServletRequest request) {
        LOGGER.info("Loading quiz with ID: {}", quizId);

        Quiz quiz = quizRepository.findById(quizId)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found", quizId, "Quiz"));
        Long ownerId = deckAccessService.getOwnerId(quiz.getDeck().getId());
        userVerificationService.verifyOwner(request, ownerId);

        LOGGER.info("Loaded {} quiz with id: {}",1,quiz);
        return assembler.toDTO(quiz);
    }

    @Override
    @Transactional
    public void deleteQuiz(long id, HttpServletRequest request) {
        LOGGER.info("Deleting quiz with ID: {}", id);
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found",id,"Quiz"));
        Long ownerId = deckAccessService.getOwnerId(quiz.getDeck().getId());
        userVerificationService.verifyOwner(request, ownerId);
        quizRepository.delete(quiz);
        LOGGER.info("Quiz with ID: {} successfully deleted", id);
    }

    @Override
    @Transactional
    public QuizDTO updateQuiz(long id, QuizRequest quizRequest, HttpServletRequest request) {
        LOGGER.info("Updating quiz with ID: {}", id);
        Quiz quiz = quizRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Quiz not found",id,"Quiz"));
        Long ownerId = deckAccessService.getOwnerId(quiz.getDeck().getId());
        userVerificationService.verifyOwner(request, ownerId);

        quiz.setQuestion(quizRequest.getQuestion());
        quiz.setCorrectAnswer(quizRequest.getCorrectAnswer());
        quiz.setBadAnswer1(quizRequest.getBadAnswer1());
        quiz.setBadAnswer2(quizRequest.getBadAnswer2());
        quiz.setBadAnswer3(quizRequest.getBadAnswer3());
        quizRepository.save(quiz);

        return assembler.toDTO(quiz);
    }

    @Override
    @Transactional
    public void saveQuiz(QuizRequest quizRequest, HttpServletRequest request) {
        LOGGER.info("Saving new quiz for deck ID: {}", quizRequest.getDeckId());
        Long ownerId = deckAccessService.getOwnerId(quizRequest.getDeckId());
        userVerificationService.verifyOwner(request, ownerId);

        Quiz quiz = new Quiz();
        quiz.setQuestion(quizRequest.getQuestion());
        quiz.setCorrectAnswer(quizRequest.getCorrectAnswer());
        quiz.setBadAnswer1(quizRequest.getBadAnswer1());
        quiz.setBadAnswer2(quizRequest.getBadAnswer2());
        quiz.setBadAnswer3(quizRequest.getBadAnswer3());
        quiz.setDeck(deckAccessService.getDeckReference(quizRequest.getDeckId()));
        quizRepository.save(quiz);

        LOGGER.info("New quiz successfully saved for deck ID: {}", quizRequest.getDeckId());
    }
}