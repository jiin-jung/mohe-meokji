package com.mohemeokji.mohemeokji.domain;

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

    @Enumerated(EnumType.STRING) // Enum의 문자열(SINGLE, MULTI) 그대로 DB에 저장
    private HouseholdType householdType;

    @Builder
    public User(String email, String nickname, HouseholdType householdType) {
        this.email = email;
        this.nickname = nickname;
        this.householdType = householdType;
    }
}