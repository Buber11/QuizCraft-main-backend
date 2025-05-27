package main.QuizCraft.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@Entity
@Data
public class Token {
    @Id()
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String token;
    private Long userId;
    private String ip;
    private String userAgent;
    private ZonedDateTime createdAt;
    private ZonedDateTime expiresAt;

    @PreUpdate
    public void setData() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
        this.expiresAt = this.createdAt.plusDays(1);
    }

    @PrePersist()
    public void setDataPersist() {
        this.createdAt = ZonedDateTime.now(ZoneId.of("UTC"));
        this.expiresAt = this.createdAt.plusDays(1);
    }

}
