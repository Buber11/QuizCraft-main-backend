package main.QuizCraft.repository;

import main.QuizCraft.model.deck.TrueOrFalse;
import main.QuizCraft.dto.TrueOrFalseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;


@Repository
public interface TrueOrFalseRepository extends JpaRepository<TrueOrFalse, Long> {

    @Query("SELECT NEW main.QuizCraft.dto.TrueOrFalseDTO(q.question," +
            " q.trueAnswer," +
            " q.falseAnswer)" +
            "FROM TrueOrFalse q " +
            "WHERE q.deck.id = :deckId")
    Page<TrueOrFalseDTO> readDTO(@Param("deckId") Long deckId, Pageable pageable);

}
