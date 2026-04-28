package com.mohemeokji.mohemeokji.dto;

import com.mohemeokji.mohemeokji.domain.AuthProvider;
import com.mohemeokji.mohemeokji.domain.HouseholdType;
import com.mohemeokji.mohemeokji.domain.User;
import com.mohemeokji.mohemeokji.domain.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String email;
    private String nickname;
    private HouseholdType householdType;
    private AuthProvider provider;
    private UserRole role;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .householdType(user.getHouseholdType())
                .provider(user.getProvider())
                .role(user.getRole())
                .build();
    }
}
