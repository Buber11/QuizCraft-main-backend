package main.QuizCraft.service.flashcard;

import main.QuizCraft.controller.FlashcardController;
import main.QuizCraft.model.deck.Flashcard;
import main.QuizCraft.model.deck.dto.FlashcardDTO;
import main.QuizCraft.projection.FlashcardProjection;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Link;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

@Component
public class FlashcardAssembler {

    public FlashcardDTO toModel(Flashcard flashcard, Long deckId){
        FlashcardDTO flashcardDTO = new FlashcardDTO(
                flashcard.getId(),
                flashcard.getFront(),
                flashcard.getBack()
        );

        return addLink(deckId, flashcardDTO);
    }

    public FlashcardDTO toModel(FlashcardProjection flashcardProjection, Long deckId){
        FlashcardDTO flashcardDTO = new FlashcardDTO(
                flashcardProjection.getId(),
                flashcardProjection.getFront(),
                flashcardProjection.getBack()
        );

        return addLink(deckId, flashcardDTO);
    }

    private FlashcardDTO addLink(Long deckId, FlashcardDTO flashcardDTO){
        Link allFlashcardsLink = linkTo(methodOn(FlashcardController.class)
                .getFlashcards(deckId, Pageable.unpaged(), null))
                .withRel("all-flashcards");
        Link updateLink = linkTo(methodOn(FlashcardController.class)
                .updateFlashcard(flashcardDTO.getId(), null, null))
                .withRel("update");
        Link deleteLink = linkTo(methodOn(FlashcardController.class)
                .deleteFlashcard(flashcardDTO.getId(), null))
                .withRel("delete");
        Link createLink = linkTo(methodOn(FlashcardController.class)
                .saveFlashcard(null, null))
                .withRel("create");
        flashcardDTO.add(allFlashcardsLink, updateLink, deleteLink, createLink);
        return flashcardDTO;
    }

}
