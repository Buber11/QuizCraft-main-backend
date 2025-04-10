package main.QuizCraft.repository;

import main.QuizCraft.model.deck.Quiz;
import main.QuizCraft.projection.QuizProjection;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {

    @Query(value = "SELECT q.question, q.correct_answer, q.bad_answer_1, q.bad_answer_2, q.bad_answer_3 " +
            "FROM quiz q WHERE q.deck_id = :deckId",
            countQuery = "SELECT COUNT(*) FROM quiz WHERE deck_id = :deckId",
            nativeQuery = true)
    Page<QuizProjection> readDTO(@Param("deckId") Long deckId, Pageable pageable);

}
