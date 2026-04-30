package com.mohemeokji.mohemeokji.domain.scan.controller;

import com.mohemeokji.mohemeokji.domain.scan.dto.ScanImageRequest;
import com.mohemeokji.mohemeokji.domain.scan.dto.ScanResultDto;
import com.mohemeokji.mohemeokji.domain.scan.service.ScanService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/scan")
@CrossOrigin(origins = "http://localhost:5173")
public class ScanController {

    private final ScanService scanService;

    @PostMapping("/analyze")
    @Operation(summary = "재료 사진 스캔", description = "base64 인코딩 이미지를 전송하면 AI가 식재료를 인식합니다.")
    public ScanResultDto analyze(@Valid @RequestBody ScanImageRequest request) {
        return scanService.analyze(request.getImage());
    }
}