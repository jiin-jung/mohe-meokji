package com.mohemeokji.mohemeokji.domain.feedback.controller;

import com.mohemeokji.mohemeokji.auth.CurrentUserProvider;
import com.mohemeokji.mohemeokji.domain.feedback.dto.FeedbackRequest;
import com.mohemeokji.mohemeokji.domain.feedback.service.FeedbackService;
import com.mohemeokji.mohemeokji.global.dto.ApiMessageResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "http://localhost:5173")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping("/me")
    @ResponseStatus(HttpStatus.CREATED)
    public ApiMessageResponse submit(@Valid @RequestBody FeedbackRequest request) {
        feedbackService.submit(currentUserProvider.getCurrentUserId(), request);
        return new ApiMessageResponse("피드백이 등록되었습니다.");
    }
}