package com.mohemeokji.mohemeokji.domain.user.service;

import com.mohemeokji.mohemeokji.domain.user.AuthProvider;
import com.mohemeokji.mohemeokji.domain.user.User;
import com.mohemeokji.mohemeokji.domain.user.UserRole;
import com.mohemeokji.mohemeokji.domain.user.dto.UserRequest;
import com.mohemeokji.mohemeokji.domain.user.dto.UserResponse;
import com.mohemeokji.mohemeokji.domain.user.exception.UserErrorCode;
import com.mohemeokji.mohemeokji.domain.user.exception.UserException;
import com.mohemeokji.mohemeokji.domain.user.repository.UserRepository;
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
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, "해당 유저가 없습니다. id=" + userId)));
    }

    @Transactional(readOnly = true)
    public List<UserResponse> findAll() {
        return userRepository.findAll().stream()
                .map(UserResponse::from)
                .toList();
    }

    @Transactional
    public UserResponse updateNickname(Long userId, String name) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, "해당 유저가 없습니다. id=" + userId));
        user.updateNickname(name);
        return UserResponse.from(user);
    }

    @Transactional
    public String updateAvatarUrl(Long userId, String avatarUrl) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, "해당 유저가 없습니다. id=" + userId));
        user.updateAvatarUrl(avatarUrl);
        return user.getAvatarUrl();
    }
}