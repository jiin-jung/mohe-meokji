package com.mohemeokji.mohemeokji.domain.feedback;

import com.mohemeokji.mohemeokji.domain.user.User;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "feedback")
public class Feedback {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(columnDefinition = "TEXT", nullable = false)
    private String content;

    @Column(nullable = false)
    private Integer rating;

    private LocalDateTime createdAt;

    @Builder
    public Feedback(User user, String content, Integer rating) {
        this.user = user;
        this.content = content;
        this.rating = rating;
        this.createdAt = LocalDateTime.now();
    }
}