package main.QuizCraft.service.deck;

import jakarta.servlet.http.HttpServletRequest;
import main.QuizCraft.dto.DeckDTO;
import main.QuizCraft.request.DeckRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DeckService {
     void saveDeck(HttpServletRequest httpServletRequest, DeckRequest deckRequest);
     void removeDeck(HttpServletRequest httpServletRequest, Long deckId);
     DeckDTO updateNameDeck(HttpServletRequest httpServletRequest, DeckRequest deckRequest, Long deckId);
     DeckDTO getDeck(HttpServletRequest httpServletRequest, Long deckId);
     Page<DeckDTO> getAllDeck(HttpServletRequest httpServletRequest, Long userId, Pageable pageable);
}
