package com.mohemeokji.mohemeokji.domain.user.dto;

import com.mohemeokji.mohemeokji.domain.user.AuthProvider;
import com.mohemeokji.mohemeokji.domain.user.HouseholdType;
import com.mohemeokji.mohemeokji.domain.user.User;
import com.mohemeokji.mohemeokji.domain.user.UserRole;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class UserResponse {
    private Long id;
    private String name;
    private String email;
    private String nickname;
    private String avatarUrl;
    private HouseholdType householdType;
    private AuthProvider provider;
    private UserRole role;

    public static UserResponse from(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .name(user.getNickname())
                .email(user.getEmail())
                .nickname(user.getNickname())
                .avatarUrl(user.getAvatarUrl())
                .householdType(user.getHouseholdType())
                .provider(user.getProvider())
                .role(user.getRole())
                .build();
    }
}