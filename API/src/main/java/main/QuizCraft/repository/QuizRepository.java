package main.QuizCraft.repository;

import main.QuizCraft.model.deck.Quiz;
import main.QuizCraft.model.deck.dto.FlashcardDTO;
import main.QuizCraft.model.deck.dto.QuizDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface QuizRepository extends JpaRepository<Quiz,Long> {

    @Query("SELECT NEW main.QuizCraft.model.deck.dto.QuizDTO(q.question," +
            " q.correctAnswer," +
            " q.badAnswer1," +
            " q.badAnswer2," +
            " q.badAnswer3) " +
            "FROM Quiz q " +
            "WHERE q.deck.id = :deckId")
    Page<QuizDTO> readDTO(@Param("deckId") Long deckId, Pageable pageable);

}
