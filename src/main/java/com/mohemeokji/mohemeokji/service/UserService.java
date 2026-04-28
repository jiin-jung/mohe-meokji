package com.mohemeokji.mohemeokji.service;

import com.mohemeokji.mohemeokji.domain.AuthProvider;
import com.mohemeokji.mohemeokji.domain.User;
import com.mohemeokji.mohemeokji.domain.UserRole;
import com.mohemeokji.mohemeokji.dto.UserRequest;
import com.mohemeokji.mohemeokji.dto.UserResponse;
import com.mohemeokji.mohemeokji.exception.EntityNotFoundException;
import com.mohemeokji.mohemeokji.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    @Transactional
    public Long join(UserRequest request) {
        User user = User.builder()
                .email(request.getEmail())
                .nickname(request.getNickname())
                .provider(AuthProvider.LOCAL)
                .role(UserRole.USER)
                .householdType(request.getHouseholdType())
                .build();
        return userRepository.save(user).getId();
    }

    @Transactional(readOnly = true)
    public UserResponse getUser(Long userId) {
        return UserResponse.from(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저가 없습니다. id=" + userId)));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }
}