package main.QuizCraft.service.deck;

import main.QuizCraft.model.Deck;

public interface DeckAccessService {
    Long getOwnerId(Long deckId);
    Deck getDeckReference(Long deckId);

}
