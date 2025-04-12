package main.QuizCraft.service.deck;

import main.QuizCraft.controller.DeckController;
import main.QuizCraft.model.Deck;
import main.QuizCraft.dto.DeckDTO;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class DeckAssembler {

    public DeckDTO toModel(Deck deck) {
        DeckDTO deckDTO = new DeckDTO(deck.getId(), deck.getName());


        deckDTO.add(linkTo(methodOn(DeckController.class).getDeck(null, deck.getId()))
                .withSelfRel()
                .withName("get"));

        deckDTO.add(linkTo(methodOn(DeckController.class).saveDeck(null, null))
                .withSelfRel()
                .withName("save"));

        deckDTO.add(linkTo(methodOn(DeckController.class).removeDeck(null, deck.getId()))
                .withRel("delete")
                .withName("delete"));

        deckDTO.add(linkTo(methodOn(DeckController.class).updateDeck(null, deck.getId(),null))
                .withRel("update")
                .withName("update"));

        deckDTO.add(linkTo(methodOn(DeckController.class).getAllDeck(null,null,null))
                .withRel("all_decks")
                .withName("getAll"));



        return deckDTO;
    }
}
