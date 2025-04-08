package main.QuizCraft.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.dto.FlashcardDTO;
import main.QuizCraft.request.FlashcardRequest;
import main.QuizCraft.service.flashcard.FlashcardService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/deck/")
@RequiredArgsConstructor
public class FlashcardController {

    private final FlashcardService flashcardService;

    @GetMapping("/{deck_id}/flashcard")
    public ResponseEntity<Page<FlashcardDTO>> getFlashcards(
            @PathVariable("deck_id") long deckId,
            @PageableDefault(size = 10,
                    sort = "front",
                    direction = Sort.Direction.DESC)
            Pageable pageable,
            HttpServletRequest httpServletRequest
    ){
        Page<FlashcardDTO> page = flashcardService.loadFlashcards(deckId,pageable,httpServletRequest);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/flashcard")
    public ResponseEntity saveFlashcard(
            @RequestBody FlashcardRequest flashcardRequest,
            HttpServletRequest httpServletRequest ){
        flashcardService.saveFlashcard(flashcardRequest, httpServletRequest);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/flashcard/{id}")
    public ResponseEntity deleteFlashcard(
            @PathVariable("id") long id,
            HttpServletRequest httpServletRequest){
        flashcardService.deleteFlashcard(id,httpServletRequest);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/flashcard/{id}")
    public ResponseEntity updateFlashcard(@PathVariable("id") long id,
                                          @RequestBody FlashcardRequest flashcardRequest,
                                          HttpServletRequest httpServletRequest
                                          ){

        FlashcardDTO flashcardDTO = flashcardService.updateFlashcard(id,flashcardRequest,httpServletRequest);
        return ResponseEntity.ok(flashcardDTO);
    }

}
