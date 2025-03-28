package main.QuizCraft.model.deck;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Objects;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "true_or_false")
public class TrueOrFalse {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "deck_id", nullable = false)
    private Deck deck;

    @Column(nullable = false, length = 255)
    private String question;

    @Column(name = "true_answer", nullable = false, length = 255)
    private String trueAnswer;

    @Column(name = "false_answer", nullable = false, length = 255)
    private String falseAnswer;

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof TrueOrFalse that)) return false;
        return Objects.equals(id, that.id);
    }

    @Override
    public int hashCode() {
        return getClass().hashCode();
    }
}
