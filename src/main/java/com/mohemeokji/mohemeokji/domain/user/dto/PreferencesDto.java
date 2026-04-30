package com.mohemeokji.mohemeokji.domain.user.dto;

import com.mohemeokji.mohemeokji.domain.user.UserSettings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class PreferencesDto {
    private Integer defaultServings;
    private List<String> allergies;
    private List<String> dislikedIngredients;

    public static PreferencesDto from(UserSettings settings) {
        return new PreferencesDto(
                settings.getDefaultServings(),
                splitToList(settings.getAllergies()),
                splitToList(settings.getDislikedIngredients())
        );
    }

    public static PreferencesDto defaults() {
        return new PreferencesDto(1, Collections.emptyList(), Collections.emptyList());
    }

    public String allergiesToString() {
        return joinToString(allergies);
    }

    public String dislikedIngredientsToString() {
        return joinToString(dislikedIngredients);
    }

    private static List<String> splitToList(String value) {
        if (value == null || value.isBlank()) return Collections.emptyList();
        return Arrays.stream(value.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .toList();
    }

    private static String joinToString(List<String> list) {
        if (list == null || list.isEmpty()) return null;
        return String.join(",", list);
    }
}