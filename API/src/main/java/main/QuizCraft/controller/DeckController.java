package main.QuizCraft.controller;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.model.deck.dto.DeckDTO;
import main.QuizCraft.model.deck.request.DeckRequest;
import main.QuizCraft.service.deck.DeckService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/deck")
public class DeckController {

    private final DeckService deckService;


    @GetMapping("/{id}")
    public ResponseEntity<DeckDTO> getDeck(HttpServletRequest request, @PathVariable("id") Long id) {
        DeckDTO deckDTO = deckService.getDeck(request, id);
        return ResponseEntity.status(200).body(deckDTO);
    }

    @GetMapping
    public ResponseEntity<Page<DeckDTO>> getAllDeck(
            @RequestParam("user_id") Long userId,
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable,
            HttpServletRequest request) {

        Page<DeckDTO> deckDTOPage = deckService.getAllDeck(request, userId, pageable);
        return ResponseEntity.status(200).body(deckDTOPage);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> removeDeck(HttpServletRequest request, @PathVariable("id") Long id) {
        deckService.removeDeck(request, id);
        return ResponseEntity.status(204).build();
    }


    @PutMapping("/{id}")
    public ResponseEntity<DeckDTO> updateDeck(HttpServletRequest request,
                                              @PathVariable("id") Long id,
                                              @RequestBody DeckRequest deckRequest) {
        DeckDTO deckDTO = deckService.updateNameDeck(request, deckRequest, id);
        return ResponseEntity.status(200).body(deckDTO);
    }


    @PostMapping
    public ResponseEntity<Void> saveDeck(HttpServletRequest request, @RequestBody DeckRequest deckRequest) {
        deckService.saveDeck(request, deckRequest);
        return ResponseEntity.status(201).build();
    }


}
