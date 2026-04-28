package com.mohemeokji.mohemeokji.dto;

import com.mohemeokji.mohemeokji.domain.HouseholdType;
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
