package main.QuizCraft.repository;

import main.QuizCraft.model.deck.Flashcard;
import main.QuizCraft.model.deck.dto.FlashcardDTO;
import main.QuizCraft.projection.FlashcardProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard,Long> {

    @Query(value = "SELECT f.id, f.front, f.back FROM flashcards f WHERE f.deck_id = :deckId",
            countQuery = "SELECT COUNT(*) FROM flashcards WHERE deck_id = :deckId",
            nativeQuery = true)
    Page<FlashcardProjection> readDTO(@Param("deckId") Long deckId, Pageable pageable);






}
