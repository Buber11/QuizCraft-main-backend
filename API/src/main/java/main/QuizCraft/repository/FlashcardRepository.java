package main.QuizCraft.repository;

import main.QuizCraft.model.deck.Flashcard;
import main.QuizCraft.model.deck.dto.FlashcardDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard,Long> {

    @Query("SELECT NEW main.QuizCraft.model.deck.dto.FlashcardDTO(f.id, f.front, f.back) " +
            "FROM Flashcard f " +
            "WHERE f.deck.id = :deckId")
    Page<FlashcardDTO> readDTO(@Param("deckId") Long deckId, Pageable pageable);


}
