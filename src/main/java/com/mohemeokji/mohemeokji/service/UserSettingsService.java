package com.mohemeokji.mohemeokji.service;

import com.mohemeokji.mohemeokji.domain.User;
import com.mohemeokji.mohemeokji.domain.UserSettings;
import com.mohemeokji.mohemeokji.dto.UserSettingsDto;
import com.mohemeokji.mohemeokji.exception.EntityNotFoundException;
import com.mohemeokji.mohemeokji.repository.UserRepository;
import com.mohemeokji.mohemeokji.repository.UserSettingsRepository;
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
                            .orElseThrow(() -> new EntityNotFoundException("해당 유저가 없습니다. id=" + userId));
                    return UserSettings.builder().user(user).build();
                });

        settings.update(
                dto.getDefaultServings(),
                dto.allergiesToString(),
                dto.dislikedIngredientsToString(),
                dto.getNotificationTime()
        );

        return UserSettingsDto.from(userSettingsRepository.save(settings));
    }
}