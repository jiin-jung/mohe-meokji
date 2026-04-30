package com.mohemeokji.mohemeokji.domain.feedback.service;

import com.mohemeokji.mohemeokji.domain.feedback.Feedback;
import com.mohemeokji.mohemeokji.domain.feedback.dto.FeedbackRequest;
import com.mohemeokji.mohemeokji.domain.feedback.repository.FeedbackRepository;
import com.mohemeokji.mohemeokji.domain.user.User;
import com.mohemeokji.mohemeokji.domain.user.exception.UserErrorCode;
import com.mohemeokji.mohemeokji.domain.user.exception.UserException;
import com.mohemeokji.mohemeokji.domain.user.repository.UserRepository;
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
                .orElseThrow(() -> new UserException(UserErrorCode.USER_NOT_FOUND, "해당 유저가 없습니다. id=" + userId));

        feedbackRepository.save(Feedback.builder()
                .user(user)
                .content(request.getContent())
                .rating(request.getRating())
                .build());
    }
}