package com.mohemeokji.mohemeokji.domain.scan.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ScanImageRequest {
    @NotBlank
    private String image;
}