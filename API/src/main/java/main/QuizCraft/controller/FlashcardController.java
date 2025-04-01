package main.QuizCraft.controller;

import jakarta.servlet.http.HttpServletRequest;
import main.QuizCraft.model.deck.Flashcard;
import main.QuizCraft.model.deck.dto.FlashcardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/deck/flashcard")
public class FlashcardController {

    @GetMapping("/{deck_id}")
    public ResponseEntity<Page<FlashcardDTO>> getFlashcards(
            @PathVariable("deck_id") long deckId,
            @PageableDefault(size = 10,
                    sort = "createdAt",
                    direction = Sort.Direction.DESC)
            Pageable pageable,
            HttpServletRequest httpServletRequest
    ){
        Page<FlashcardDTO> page = null;

        return ResponseEntity.ok(page);
    }

    @PostMapping
    public ResponseEntity saveFlashcard(
            @RequestBody FlashcardDTO flashcardDTO,
            HttpServletRequest httpServletRequest ){

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}")
    public ResponseEntity deleteFlashcard(
            @PathVariable("id") long id,
            HttpServletRequest httpServletRequest){

        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity updateFlashcard(@PathVariable("id") long id,
                                          @RequestBody FlashcardDTO flashcardDTO,
                                          HttpServletRequest httpServletRequest
                                          ){


        return ResponseEntity.ok(null);
    }

}
