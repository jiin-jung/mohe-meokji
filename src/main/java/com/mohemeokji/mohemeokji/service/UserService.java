package com.mohemeokji.mohemeokji.service;

import com.mohemeokji.mohemeokji.domain.User;
import com.mohemeokji.mohemeokji.dto.UserRequest;
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
                .householdType(request.getHouseholdType())
                .build();
        return userRepository.save(user).getId();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }
}