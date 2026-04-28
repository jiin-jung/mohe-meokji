package com.mohemeokji.mohemeokji.controller;

import com.mohemeokji.mohemeokji.auth.CurrentUserProvider;
import com.mohemeokji.mohemeokji.dto.IdResponse;
import com.mohemeokji.mohemeokji.dto.UserRequest;
import com.mohemeokji.mohemeokji.dto.UserResponse;
import com.mohemeokji.mohemeokji.dto.UserSettingsDto;
import com.mohemeokji.mohemeokji.service.UserService;
import com.mohemeokji.mohemeokji.service.UserSettingsService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private final UserService userService;
    private final UserSettingsService userSettingsService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping("/signup")
    @Operation(summary = "회원 가입", description = "새로운 유저를 등록합니다.")
    public IdResponse join(@Valid @RequestBody UserRequest request) {
        Long userId = userService.join(request);
        return new IdResponse(userId);
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
}
