package main.QuizCraft.repository;

import main.QuizCraft.model.Flashcard;
import main.QuizCraft.projection.FlashcardProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;


@Repository
public interface FlashcardRepository extends JpaRepository<Flashcard,Long> {

    @Query(value = "SELECT f.id, f.front, f.back FROM flashcard f WHERE f.deck_id = :deckId",
            countQuery = "SELECT COUNT(*) FROM flashcard WHERE deck_id = :deckId",
            nativeQuery = true)
    Page<FlashcardProjection> loadDTO(@Param("deckId") Long deckId, Pageable pageable);

    @Query(value = "SELECT f.deck_id FROM flashcard f WHERE f.id :id", nativeQuery = true)
    Optional<Long> loadDeckId(@Param("id") Long id);




}
