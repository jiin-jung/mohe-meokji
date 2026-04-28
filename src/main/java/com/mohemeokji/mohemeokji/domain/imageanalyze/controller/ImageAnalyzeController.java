package com.mohemeokji.mohemeokji.domain.imageanalyze.controller;

import com.mohemeokji.mohemeokji.domain.imageanalyze.dto.ImageAnalyzeResDto;
import com.mohemeokji.mohemeokji.domain.imageanalyze.service.ImageAnalyzeService;
import com.mohemeokji.mohemeokji.global.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/analyze")
@CrossOrigin(origins = "http://localhost:5173")
public class ImageAnalyzeController implements ImageAnalyzeControllerDocs {

    private final ImageAnalyzeService imageAnalyzeService;

    @PostMapping(value = "/image", consumes = "multipart/form-data")
    @Override
    public ApiResponse<ImageAnalyzeResDto> analyzeImage(
            @RequestParam("image") MultipartFile image
    ) {
        ImageAnalyzeResDto result = imageAnalyzeService.analyzeImage(image);
        return ApiResponse.success(result);
    }
}