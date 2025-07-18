package ru.misha.tgBot.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "feedbacks")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Текст отзыва */
    @Column(nullable = false, length = 2000)
    private String message;

    /** true = положительный, false = отрицательный */
    @Column(nullable = false)
    private boolean positive;

    /** Когда создан */
    @Column(nullable = false)
    private Instant createdAt = Instant.now();

    // --- конструкторы, геттеры/сеттеры ---

    public Feedback() {}

    public Feedback(String message, boolean positive) {
        this.message = message;
        this.positive = positive;
    }

    public Long getId() { return id; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public boolean isPositive() { return positive; }
    public void setPositive(boolean positive) { this.positive = positive; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}

