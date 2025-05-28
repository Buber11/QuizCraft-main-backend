package main.QuizCraft.service.flashcard;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.exception.ResourceNotFoundException;
import main.QuizCraft.model.Flashcard;
import main.QuizCraft.dto.FlashcardDTO;
import main.QuizCraft.request.FlashcardRequest;
import main.QuizCraft.repository.FlashcardRepository;
import main.QuizCraft.service.Auth.UserVerificationService;
import main.QuizCraft.service.deck.DeckAccessService;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.slf4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
@RequiredArgsConstructor
public class FlashcardServiceImpl implements FlashcardService{

    private final FlashcardRepository flashcardRepository;
    private final UserVerificationService userVerificationService;
    private final DeckAccessService deckAccessService;
    private final FlashcardAssembler flashcardAssembler;

    private static final Logger LOGGER = LoggerFactory.getLogger(FlashcardServiceImpl.class);
    
    @Override
    @Transactional(readOnly = true)
    public Page<FlashcardDTO> loadFlashcards(long deckID, Pageable pageable, HttpServletRequest request) {
        LOGGER.info("Loading flashcards for deck ID: {}", deckID);

        verifyUserAccess(request, deckID);

        Page<FlashcardDTO> flashcards = flashcardRepository.loadDTO(deckID, pageable)
                .map(projection -> flashcardAssembler.toModel(projection, deckID));
        LOGGER.info("Loaded {} flashcards for deck ID: {}", flashcards.getTotalElements(), deckID);
        return flashcards;
    }

    @Override
    @Transactional
    public void deleteFlashcard(long id, HttpServletRequest request) {
        LOGGER.info("Deleting flashcard with ID: {}", id);
        Long deckId = getDeckId(id);

        verifyUserAccess(request, deckId);

        flashcardRepository.deleteById(id);
        LOGGER.info("Flashcard with ID: {} successfully deleted", id);
    }

    /**
     * Updates the flashcard entity.
     *
     * Note:
     * Since the flashcard is loaded and managed within the current transaction,
     * there is no need to explicitly call `save()` after modifying its fields.
     *
     * Hibernate's dirty checking mechanism will automatically detect changes
     * and flush the updates to the database upon transaction commit.
     *
     * Explicitly calling `save()` here could cause unnecessary flushes,
     * potentially reducing performance.
     */
    @Override
    @Transactional
    public FlashcardDTO updateFlashcard(long id, FlashcardRequest flashcardRequest, HttpServletRequest request) {
        LOGGER.info("Updating flashcard with ID: {}", id);
        Long deckId = getDeckId(id);

        verifyUserAccess(request, deckId);

        Flashcard flashcard = flashcardRepository.findById(id)
                .orElseThrow(() -> {
                    LOGGER.warn("Flashcard not found for ID: {}", id);
                    return new ResourceNotFoundException("Flashcard not found",id,"Flashcard");
                });

        flashcard.setFront(flashcardRequest.getFront());
        flashcard.setBack(flashcardRequest.getBack());
        LOGGER.info("Flashcard with ID: {} successfully updated", id);
        return new FlashcardDTO(flashcard.getId(), flashcard.getFront(), flashcard.getBack());
    }

    @Override
    @Transactional
    public void saveFlashcard(FlashcardRequest flashcardRequest, HttpServletRequest request) {
        LOGGER.info("Saving new flashcard for deck ID: {}", flashcardRequest.getDeckId());
        Long deckId = flashcardRequest.getDeckId();

        verifyUserAccess(request, deckId);

        var deckReference = deckAccessService.getDeckReference(deckId);

        Flashcard flashcard = Flashcard.builder()
                .front(flashcardRequest.getFront())
                .back(flashcardRequest.getBack())
                .deck(deckReference)
                .build();

        flashcardRepository.save(flashcard);
        LOGGER.debug("New flashcard successfully saved for deck ID: {}", flashcardRequest.getDeckId());
    }

    private Long getDeckId(Long id) {
        LOGGER.debug("Fetching deck ID for flashcard ID: {}", id);
        return flashcardRepository.loadDeckId(id)
                .orElseThrow(() -> {
                    LOGGER.warn("Flashcard not found for ID: {}", id);
                    return new ResourceNotFoundException("Flashcard not found",id,"Flashcard");
                });
    }

    private void verifyUserAccess(HttpServletRequest request, Long deckId) {
        Long userId = deckAccessService.getOwnerId(deckId);
        userVerificationService.verifyOwner(request, userId);
    }

}
