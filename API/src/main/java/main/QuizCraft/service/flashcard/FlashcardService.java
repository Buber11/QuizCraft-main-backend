package main.QuizCraft.service.flashcard;

import jakarta.servlet.http.HttpServletRequest;
import main.QuizCraft.dto.FlashcardDTO;
import main.QuizCraft.request.FlashcardRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface FlashcardService {

    Page<FlashcardDTO> loadFlashcards(long deckID,
                                      Pageable pageable,
                                      HttpServletRequest httpServletRequest);

    void deleteFlashcard(long id,
                         HttpServletRequest httpServletRequest);

    FlashcardDTO updateFlashcard(long id,
                         FlashcardRequest flashcardRequest,
                         HttpServletRequest httpServletRequest);

    void saveFlashcard(FlashcardRequest flashcardRequest,
                       HttpServletRequest httpServletRequest);

}
