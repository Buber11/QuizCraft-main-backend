package main.QuizCraft.service.deck;

import main.QuizCraft.model.deck.Deck;

public interface DeckAccessService {
    Long getOwnerId(Long deckId);
    Deck getDeckReference(Long deckId);

}
