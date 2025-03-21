package main.QuizCraft.repository;

import main.QuizCraft.model.deck.Deck;
import main.QuizCraft.model.user.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.expression.spel.ast.OpInc;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User,Long> {

    Optional<User> findByUsername(String username);

    @Query("SELECT u FROM User u JOIN FETCH u.decks WHERE u.username = :username")
    Optional<User> findByUsernameWithDeck(@Param("username") String username);


}
