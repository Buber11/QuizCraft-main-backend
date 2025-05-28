package main.QuizCraft.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Objects;


@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Flashcard {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @Column(nullable = false)
    private String front;

    @Column(nullable = false)
    private String back;

    public Flashcard(String front, String back) {
        this.front = front;
        this.back = back;
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Flashcard flashcard)) return false;
        return Objects.equals(id, flashcard.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
