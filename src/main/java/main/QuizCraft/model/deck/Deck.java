package main.QuizCraft.model.deck;

import jakarta.persistence.*;
import lombok.*;
import main.QuizCraft.model.user.User;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@NamedEntityGraph(name = "Deck.withQuizzes",
        attributeNodes = @NamedAttributeNode("quizzes")
)
@NamedEntityGraph(name = "Deck.withFlashcards",
        attributeNodes = @NamedAttributeNode("flashcards")
)
@NamedEntityGraph(name = "Deck.withUser",
        attributeNodes = @NamedAttributeNode("owner")
)
public class Deck {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User owner;

    @OneToMany(mappedBy = "deck",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Flashcard> flashcards;

    @OneToMany(mappedBy = "deck",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<Quiz> quizzes;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Deck deck)) return false;
        return Objects.equals(id, deck.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }

    public void addFlashcard(Flashcard flashcard) {
        if (flashcards == null) {
            flashcards = new ArrayList<>();
        }
        flashcards.add(flashcard);
        flashcard.setDeck(this);
    }

    public void addFlashcards(List<Flashcard> flashcards) {
        if (flashcards != null) {
            flashcards.forEach(this::addFlashcard);
        }
    }

    public void removeFlashcard(Flashcard flashcard) {
        if (flashcards != null) {
            flashcards.remove(flashcard);
            flashcard.setDeck(null);
        }
    }

    public void removeFlashcards(List<Flashcard> flashcards) {
        if (flashcards != null) {
            flashcards.forEach(this::removeFlashcard);
        }
    }

    public void addQuiz(Quiz quiz) {
        if (quizzes == null) {
            quizzes = new ArrayList<>();
        }
        quizzes.add(quiz);
        quiz.setDeck(this);
    }

    public void addQuizzes(List<Quiz> quizzes) {
        if (quizzes != null) {
            quizzes.forEach(this::addQuiz);
        }
    }

    public void removeQuiz(Quiz quiz) {
        if (quizzes != null) {
            quizzes.remove(quiz);
            quiz.setDeck(null);
        }
    }

    public void removeQuizzes(List<Quiz> quizzes) {
        if (quizzes != null) {
            quizzes.forEach(this::removeQuiz);
        }
    }





}
