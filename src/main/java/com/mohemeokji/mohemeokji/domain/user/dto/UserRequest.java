package com.mohemeokji.mohemeokji.domain.user.dto;

import com.mohemeokji.mohemeokji.domain.user.HouseholdType;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class UserRequest {
    @Email
    @NotBlank
    private String email;

    @NotBlank
    private String nickname;

    @NotNull
    private HouseholdType householdType;
}