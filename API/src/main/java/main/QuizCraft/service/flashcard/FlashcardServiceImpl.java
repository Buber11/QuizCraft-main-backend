package main.QuizCraft.service.flashcard;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import main.QuizCraft.model.deck.dto.FlashcardDTO;
import main.QuizCraft.model.deck.request.FlashcardRequest;
import main.QuizCraft.repository.FlashcardRepository;
import main.QuizCraft.service.Auth.UserVerificationService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class FlashcardServiceImpl implements FlashcardService{

    private final FlashcardRepository flashcardRepository;
    private final UserVerificationService userVerificationService;


    @Override
    public Page<FlashcardDTO> loadFlashcards(long deckID,
                                             Pageable pageable,
                                             HttpServletRequest httpServletRequest) {

        return null;
    }

    @Override
    public void deleteFlashcard(long id,
                                HttpServletRequest httpServletRequest) {

    }

    @Override
    public FlashcardDTO updateFlashcard(long id,
                                        FlashcardRequest flashcardRequest,
                                        HttpServletRequest httpServletRequest) {
        return null;
    }

    @Override
    public void saveFlashcard(FlashcardRequest flashcardRequest) {

    }
}
