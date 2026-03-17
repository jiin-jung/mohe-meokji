package com.mohemeokji.mohemeokji.dto;

import com.mohemeokji.mohemeokji.domain.HouseholdType;
import lombok.Getter;

@Getter
public class UserRequest {
    private String email;
    private String nickname;
    private HouseholdType householdType;
}