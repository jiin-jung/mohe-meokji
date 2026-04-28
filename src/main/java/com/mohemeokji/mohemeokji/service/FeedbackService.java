package com.mohemeokji.mohemeokji.service;

import com.mohemeokji.mohemeokji.domain.Feedback;
import com.mohemeokji.mohemeokji.domain.User;
import com.mohemeokji.mohemeokji.dto.FeedbackRequest;
import com.mohemeokji.mohemeokji.exception.EntityNotFoundException;
import com.mohemeokji.mohemeokji.repository.FeedbackRepository;
import com.mohemeokji.mohemeokji.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class FeedbackService {

    private final FeedbackRepository feedbackRepository;
    private final UserRepository userRepository;

    @Transactional
    public void submit(Long userId, FeedbackRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("해당 유저가 없습니다. id=" + userId));

        feedbackRepository.save(Feedback.builder()
                .user(user)
                .content(request.getContent())
                .rating(request.getRating())
                .build());
    }
}