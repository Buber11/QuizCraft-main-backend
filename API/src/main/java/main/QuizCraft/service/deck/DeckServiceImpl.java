package main.QuizCraft.service.deck;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.exception.ResourceNotFoundException;
import main.QuizCraft.exception.UserNotFoundException;
import main.QuizCraft.model.deck.Deck;
import main.QuizCraft.model.deck.dto.DeckDTO;
import main.QuizCraft.model.deck.request.DeckRequest;
import main.QuizCraft.model.user.User;
import main.QuizCraft.repository.DeckRepository;
import main.QuizCraft.repository.UserRepository;
import main.QuizCraft.security.JwtService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class DeckServiceImpl implements DeckService, DeckOwnerSupplierService{

    private final DeckRepository deckRepository;
    private final UserRepository userRepository;
    private final JwtService jwtService;
    private final DeckAssembler deckAssembler;

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

        checkAccessForUser(request, owner.getId());

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

        Deck deck = deckRepository.findDeckByIdWithUser(deckId)
                .orElseThrow(() -> {
                    logger.warn("Deck not found for id: {}", deckId);
                    return new ResourceNotFoundException("Deck not found");
                });

        logger.info("Deck found with name: {}", deck.getName());

        checkAccessForUser(httpServletRequest, deck.getOwner().getId());

        deckRepository.delete(deck);
        logger.info("Deck removed successfully with id: {}", deckId);
    }

    @Override
    @Transactional
    public DeckDTO updateNameDeck(HttpServletRequest request, DeckRequest deckRequest, Long deckId) {
        logger.info("Attempting to update deck with id: {} and new name: {}", deckId, deckRequest.getName());

        Deck deck = deckRepository.findDeckByIdWithUser(deckId)
                .orElseThrow(() -> {
                    logger.warn("Deck not found for id: {}", deckId);
                    return new ResourceNotFoundException("Deck not found");
                });

        logger.info("Deck found for update: {}", deck.getName());

        checkAccessForUser(request, deck.getOwner().getId());

        deck.setName(deckRequest.getName());
        deck = deckRepository.save(deck);

        DeckDTO deckDTO = deckAssembler.toModel(deck);
        logger.info("Deck updated successfully with new name: {}", deck.getName());

        return deckDTO;
    }

    @Override
    @Transactional
    public DeckDTO getDeck(HttpServletRequest httpServletRequest, Long deckId) {
        logger.info("Attempting to retrieve deck with id: {}", deckId);

        Deck deck = deckRepository.findDeckByIdWithUser(deckId)
                .orElseThrow(() -> {
                    logger.warn("Deck not found for id: {}", deckId);
                    return new ResourceNotFoundException("Deck not found");
                });

        logger.info("Deck found with name: {}", deck.getName());

        checkAccessForUser(httpServletRequest, deck.getOwner().getId());

        DeckDTO deckDTO = deckAssembler.toModel(deck);
        logger.info("Deck retrieved successfully with name: {}", deck.getName());

        return deckDTO;
    }

    @Override
    @Transactional
    public Page<DeckDTO> getAllDeck(HttpServletRequest httpServletRequest, Long userId, Pageable pageable) {
        logger.info("Attempting to retrieve all decks for user with id: {}", userId);

        checkAccessForUser(httpServletRequest, userId);
        Page<Deck> decksPage = deckRepository.findByOwnerId(userId, pageable);

        logger.info("Found {} decks for user with id: {}", decksPage.getTotalElements(), userId);

        return decksPage.map(deckAssembler::toModel);
    }

    private void checkAccessForUser(HttpServletRequest request, Long userRequestId){
        Long currentUserId = getCurrentUserId(request);
        if (!currentUserId.equals(userRequestId)) {
            logger.error("Access denied: User with id {} is not authorized to access this deck.", currentUserId);
            throw new AccessDeniedException("You are not the owner of this deck");
        }
        logger.info("Access granted for user with id: {}", currentUserId);
    }

    private Long getCurrentUserId(HttpServletRequest httpServletRequest){
        Long userId = (Long) httpServletRequest.getAttribute("user_id");
        logger.debug("Current user id from request: {}", userId);
        return userId;
    }

    @Override
    public Long getOwnerId(Long deckId) {

        return 0L;
    }
}
