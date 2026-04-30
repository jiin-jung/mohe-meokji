package com.mohemeokji.mohemeokji.domain.user;

import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
@Table(name = "users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String email;

    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private AuthProvider provider;

    private String providerUserId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private UserRole role;

    @Enumerated(EnumType.STRING)
    private HouseholdType householdType;

    @Column(columnDefinition = "MEDIUMTEXT")
    private String avatarUrl;

    @Builder
    public User(String email, String nickname, AuthProvider provider, String providerUserId, UserRole role, HouseholdType householdType) {
        this.email = email;
        this.nickname = nickname;
        this.provider = provider;
        this.providerUserId = providerUserId;
        this.role = role;
        this.householdType = householdType;
    }

    public void updateNickname(String nickname) {
        this.nickname = nickname;
    }

    public void updateAvatarUrl(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }
}