package com.mohemeokji.mohemeokji.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class FeedbackRequest {
    @NotBlank
    private String content;

    @NotNull
    @Min(1) @Max(5)
    private Integer rating;
}