package com.mohemeokji.mohemeokji.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class AvatarUploadRequest {
    @NotBlank
    private String image;
}