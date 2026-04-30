package com.mohemeokji.mohemeokji.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "user_settings")
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false, unique = true)
    private User user;

    private Integer defaultServings;

    @Column(columnDefinition = "TEXT")
    private String allergies;

    @Column(columnDefinition = "TEXT")
    private String dislikedIngredients;

    private String notificationTime;

    @Builder
    public UserSettings(User user, Integer defaultServings, String allergies, String dislikedIngredients, String notificationTime) {
        this.user = user;
        this.defaultServings = defaultServings;
        this.allergies = allergies;
        this.dislikedIngredients = dislikedIngredients;
        this.notificationTime = notificationTime;
    }

    public void update(Integer defaultServings, String allergies, String dislikedIngredients, String notificationTime) {
        this.defaultServings = defaultServings;
        this.allergies = allergies;
        this.dislikedIngredients = dislikedIngredients;
        this.notificationTime = notificationTime;
    }
}