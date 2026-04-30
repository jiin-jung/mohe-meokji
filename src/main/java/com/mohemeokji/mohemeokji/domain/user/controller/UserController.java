package com.mohemeokji.mohemeokji.domain.user.controller;

import com.mohemeokji.mohemeokji.auth.CurrentUserProvider;
import com.mohemeokji.mohemeokji.domain.user.dto.*;
import com.mohemeokji.mohemeokji.domain.user.service.UserService;
import com.mohemeokji.mohemeokji.domain.user.service.UserSettingsService;
import com.mohemeokji.mohemeokji.global.dto.IdResponse;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping({"/api/users", "/api/user"})
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {

    private final UserService userService;
    private final UserSettingsService userSettingsService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입")
    public IdResponse join(@Valid @RequestBody UserRequest request) {
        return new IdResponse(userService.join(request));
    }

    @GetMapping("/me")
    @Operation(summary = "현재 유저 조회")
    public UserResponse me() {
        return userService.getUser(currentUserProvider.getCurrentUserId());
    }

    @GetMapping
    @Operation(summary = "전체 유저 조회")
    public List<UserResponse> findAll() {
        return userService.findAll();
    }

    @GetMapping("/settings/me")
    @Operation(summary = "식단 설정 조회")
    public UserSettingsDto getSettings() {
        return userSettingsService.getSettings(currentUserProvider.getCurrentUserId());
    }

    @PutMapping("/settings/me")
    @Operation(summary = "식단 설정 저장")
    public UserSettingsDto saveSettings(@RequestBody UserSettingsDto dto) {
        return userSettingsService.saveSettings(currentUserProvider.getCurrentUserId(), dto);
    }

    @PatchMapping("/me")
    @Operation(summary = "프로필 이름 변경")
    public UserResponse updateProfile(@Valid @RequestBody UserProfileUpdateRequest request) {
        return userService.updateNickname(currentUserProvider.getCurrentUserId(), request.getName());
    }

    @PostMapping("/me/avatar")
    @Operation(summary = "프로필 사진 업로드")
    public AvatarResponse uploadAvatar(@Valid @RequestBody AvatarUploadRequest request) {
        String avatarUrl = userService.updateAvatarUrl(currentUserProvider.getCurrentUserId(), request.getImage());
        return new AvatarResponse(avatarUrl);
    }

    @GetMapping("/me/preferences")
    @Operation(summary = "식단 선호 설정 조회")
    public PreferencesDto getPreferences() {
        return userSettingsService.getPreferences(currentUserProvider.getCurrentUserId());
    }

    @PatchMapping("/me/preferences")
    @Operation(summary = "식단 선호 설정 변경")
    public PreferencesDto patchPreferences(@RequestBody PreferencesDto dto) {
        return userSettingsService.patchPreferences(currentUserProvider.getCurrentUserId(), dto);
    }
}