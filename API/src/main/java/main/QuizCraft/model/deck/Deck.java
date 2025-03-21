package main.QuizCraft.model.deck;

import jakarta.persistence.*;
import lombok.*;
import main.QuizCraft.model.user.User;

import java.util.ArrayList;
import java.util.List;

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

@NamedEntityGraph(name = "Deck.withTrueOrFalseQuestions",
        attributeNodes = @NamedAttributeNode("trueOrFalseQuestions")
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

    @OneToMany(mappedBy = "deck",
            cascade = CascadeType.ALL,
            orphanRemoval = true,
            fetch = FetchType.LAZY
    )
    private List<TrueOrFalse> trueOrFalseQuestions;

}
