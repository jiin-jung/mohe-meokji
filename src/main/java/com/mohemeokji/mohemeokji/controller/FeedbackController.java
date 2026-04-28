package com.mohemeokji.mohemeokji.controller;

import com.mohemeokji.mohemeokji.auth.CurrentUserProvider;
import com.mohemeokji.mohemeokji.dto.ApiMessageResponse;
import com.mohemeokji.mohemeokji.dto.FeedbackRequest;
import com.mohemeokji.mohemeokji.service.FeedbackService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/feedback")
@CrossOrigin(origins = "http://localhost:5173")
public class FeedbackController {

    private final FeedbackService feedbackService;
    private final CurrentUserProvider currentUserProvider;

    @PostMapping("/me")
    public ApiMessageResponse submit(@Valid @RequestBody FeedbackRequest request) {
        feedbackService.submit(currentUserProvider.getCurrentUserId(), request);
        return new ApiMessageResponse("피드백이 등록되었습니다.");
    }
}