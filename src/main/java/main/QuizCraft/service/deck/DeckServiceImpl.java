package main.QuizCraft.service.deck;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.exception.ResourceNotFoundException;
import main.QuizCraft.exception.UserNotFoundException;
import main.QuizCraft.model.Deck;
import main.QuizCraft.dto.DeckDTO;
import main.QuizCraft.request.DeckRequest;
import main.QuizCraft.model.User;
import main.QuizCraft.repository.DeckRepository;
import main.QuizCraft.repository.UserRepository;
import main.QuizCraft.service.Auth.UserVerificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeckServiceImpl implements DeckService, DeckAccessService {

    private final DeckRepository deckRepository;
    private final UserRepository userRepository;
    private final DeckAssembler deckAssembler;
    private final UserVerificationService userVerificationService;

    private final Logger logger = LoggerFactory.getLogger(DeckServiceImpl.class);

    @Override
    @Transactional
    public void saveDeck(HttpServletRequest request, DeckRequest deckRequest) {
        logger.info("Attempting to save deck with name: {}", deckRequest.getName());

        User owner = userRepository.findById(deckRequest.getUserId())
                .orElseThrow(() -> {
                    logger.warn("User not found for id: {}", deckRequest.getUserId());
                    return new UserNotFoundException("User not found");
                });

        logger.info("User found for deck creation: {}", owner.getUsername());

        userVerificationService.verifyOwner(request, owner.getId());

        Deck deck = Deck.builder()
                .owner(owner)
                .name(deckRequest.getName())
                .build();

        deckRepository.save(deck);
        logger.info("Deck saved successfully with name: {}", deck.getName());
    }

    @Override
    @Transactional
    public void removeDeck(HttpServletRequest httpServletRequest, Long deckId) {
        logger.info("Attempting to remove deck with id: {}", deckId);

        Deck deck = getDeck(deckId);

        userVerificationService.verifyOwner(httpServletRequest, deck.getOwner().getId());

        deckRepository.delete(deck);
        logger.info("Deck removed successfully with id: {}", deckId);
    }

    @Override
    @Transactional
    public DeckDTO updateNameDeck(HttpServletRequest httpServletRequest, DeckRequest deckRequest, Long deckId) {
        logger.info("Attempting to update deck with id: {} and new name: {}", deckId, deckRequest.getName());

        Deck deck = getDeck(deckId);

        userVerificationService.verifyOwner(httpServletRequest, deck.getOwner().getId());

        deck.setName(deckRequest.getName());
        deck = deckRepository.save(deck);

        DeckDTO deckDTO = deckAssembler.toModel(deck);
        logger.info("Deck updated successfully with new name: {}", deck.getName());

        return deckDTO;
    }

    @Override
    @Transactional(readOnly = true)
    public DeckDTO getDeck(HttpServletRequest httpServletRequest, Long deckId) {
        logger.info("Attempting to retrieve deck with id: {}", deckId);

        Deck deck = getDeck(deckId);

        userVerificationService.verifyOwner(httpServletRequest, deck.getOwner().getId());

        DeckDTO deckDTO = deckAssembler.toModel(deck);
        logger.info("Deck retrieved successfully with name: {}", deck.getName());

        return deckDTO;
    }


    @Override
    @Transactional(readOnly = true)
    public Page<DeckDTO> getAllDeck(HttpServletRequest httpServletRequest, Long userId, Pageable pageable) {
        logger.info("Attempting to retrieve all decks for user with id: {}", userId);

        userVerificationService.verifyOwner(httpServletRequest, userId);

        Page<Deck> decksPage = deckRepository.findByOwnerId(userId, pageable);

        logger.info("Found {} decks for user with id: {}", decksPage.getTotalElements(), userId);

        return decksPage.map(deckAssembler::toModel);
    }

    private Deck getDeck(Long deckId){
        Deck deck = deckRepository.findDeckByIdWithUser(deckId)
                .orElseThrow(() -> {
                    logger.warn("Deck not found for id: {}", deckId);
                    return new ResourceNotFoundException("Deck not found", deckId, "Deck");
                });

        logger.info("Deck found with name: {}", deck.getName());

        return deck;
    }

    @Override
    @Cacheable("deckOwnerCache")
    public Long getOwnerId(Long deckId) {
        return deckRepository.findOwnerId(deckId)
                .orElseThrow(() -> {
                    logger.warn("Deck not found for id: {}", deckId);
                    return new ResourceNotFoundException("Deck not found", deckId, "Deck");
                });

    }

    @Override
    public Deck getDeckReference(Long deckId) {
        return deckRepository.getReferenceById(deckId);
    }
}
