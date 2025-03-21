package main.QuizCraft.model.deck;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Quiz {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @Column(nullable = false, length = 255)
    private String question;

    @Column(name = "correct_answer", nullable = false, length = 255)
    private String correctAnswer;

    @Column(name = "bad_answer_1", nullable = false, length = 255)
    private String badAnswer1;

    @Column(name = "bad_answer_2", nullable = false, length = 255)
    private String badAnswer2;

    @Column(name = "bad_answer_3", nullable = false, length = 255)
    private String badAnswer3;
}
