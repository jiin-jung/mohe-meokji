package com.mohemeokji.mohemeokji.controller;

import com.mohemeokji.mohemeokji.domain.User;
import com.mohemeokji.mohemeokji.dto.UserRequest;
import com.mohemeokji.mohemeokji.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173")
public class UserController {
    private final UserService userService;

    @PostMapping("/join")
    @Operation(summary = "회원 가입", description = "새로운 유저를 등록합니다.")
    public String join(@RequestBody UserRequest request) {
        Long userId = userService.join(request);
        return "가입 성공! ID: " + userId;
    }

    @GetMapping("/all")
    @Operation(summary = "전체 유저 조회")
    public List<User> findAll() {
        return userService.findAll();
    }
}