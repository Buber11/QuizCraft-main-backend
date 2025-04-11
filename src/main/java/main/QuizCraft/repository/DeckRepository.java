package main.QuizCraft.repository;

import main.QuizCraft.model.Deck;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface DeckRepository extends JpaRepository<Deck,Long> {

    @Query(
            value = "SELECT d FROM Deck d WHERE d.owner.id = :userId",
            countQuery = "SELECT COUNT(d) FROM Deck d WHERE d.owner.id = :userId"
    )
    Page<Deck> findByOwnerId(@Param("userId") Long userId, Pageable pageable);

    @EntityGraph(value = "Deck.withQuizzes",
            type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT d FROM Deck d WHERE d.id = :id")
    Optional<Deck> findDeckByIdWithQuizzes(@Param("id") Long id);

    @EntityGraph(value = "Deck.withFlashcards",
            type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT d FROM Deck d WHERE d.id = :id")
    Optional<Deck> findDeckByIdWithFlashcards(@Param("id") Long id);

    @EntityGraph(value = "Deck.withTrueOrFalseQuestions",
            type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT d FROM Deck d WHERE d.id = :id")
    Optional<Deck> findDeckByIdWithTrueOrFalseQuestions(@Param("id") Long id);

    @EntityGraph(value = "Deck.withUser",
            type = EntityGraph.EntityGraphType.LOAD)
    @Query("SELECT d FROM Deck d WHERE d.id = :id")
    Optional<Deck> findDeckByIdWithUser(@Param("id") Long id);

    @Query(value = "SELECT d.user_id FROM deck d WHERE d.id = :id", nativeQuery = true)
    Optional<Long> findOwnerId(@Param("id")Long id);

}
