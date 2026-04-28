package com.mohemeokji.mohemeokji.dto;

import com.mohemeokji.mohemeokji.domain.UserSettings;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UserSettingsDto {
    private Integer defaultServings;
    private List<String> allergies;
    private List<String> dislikedIngredients;
    private String notificationTime;

    public static UserSettingsDto from(UserSettings settings) {
        return new UserSettingsDto(
                settings.getDefaultServings(),
                splitToList(settings.getAllergies()),
                splitToList(settings.getDislikedIngredients()),
                settings.getNotificationTime()
        );
    }

    public static UserSettingsDto defaultSettings() {
        return new UserSettingsDto(1, Collections.emptyList(), Collections.emptyList(), null);
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