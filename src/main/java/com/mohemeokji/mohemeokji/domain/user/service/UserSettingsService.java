package com.mohemeokji.mohemeokji.domain.user.service;

import com.mohemeokji.mohemeokji.domain.user.User;
import com.mohemeokji.mohemeokji.domain.user.UserSettings;
import com.mohemeokji.mohemeokji.domain.user.dto.PreferencesDto;
import com.mohemeokji.mohemeokji.domain.user.dto.UserSettingsDto;
import com.mohemeokji.mohemeokji.domain.user.exception.UserErrorCode;
import com.mohemeokji.mohemeokji.domain.user.exception.UserException;
import com.mohemeokji.mohemeokji.domain.user.repository.UserRepository;
import com.mohemeokji.mohemeokji.domain.user.repository.UserSettingsRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserSettingsService {

    private final UserSettingsRepository userSettingsRepository;
    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public UserSettingsDto getSettings(Long userId) {
        return userSettingsRepository.findByUserId(userId)
                .map(UserSettingsDto::from)
                .orElse(UserSettingsDto.defaultSettings());
    }

    @Transactional
    public UserSettingsDto saveSettings(Long userId, UserSettingsDto dto) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, "해당 유저가 없습니다. id=" + userId));
                    return UserSettings.builder().user(user).build();
                });
        settings.update(dto.getDefaultServings(), dto.allergiesToString(), dto.dislikedIngredientsToString(), dto.getNotificationTime());
        return UserSettingsDto.from(userSettingsRepository.save(settings));
    }

    @Transactional(readOnly = true)
    public PreferencesDto getPreferences(Long userId) {
        return userSettingsRepository.findByUserId(userId)
                .map(PreferencesDto::from)
                .orElse(PreferencesDto.defaults());
    }

    @Transactional
    public PreferencesDto patchPreferences(Long userId, PreferencesDto dto) {
        UserSettings settings = userSettingsRepository.findByUserId(userId)
                .orElseGet(() -> {
                    User user = userRepository.findById(userId)
                            .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, "해당 유저가 없습니다. id=" + userId));
                    return UserSettings.builder().user(user).build();
                });
        Integer newServings = dto.getDefaultServings() != null ? dto.getDefaultServings() : settings.getDefaultServings();
        String newAllergies = dto.getAllergies() != null ? dto.allergiesToString() : settings.getAllergies();
        String newDisliked = dto.getDislikedIngredients() != null ? dto.dislikedIngredientsToString() : settings.getDislikedIngredients();
        settings.update(newServings, newAllergies, newDisliked, settings.getNotificationTime());
        return PreferencesDto.from(userSettingsRepository.save(settings));
    }
}