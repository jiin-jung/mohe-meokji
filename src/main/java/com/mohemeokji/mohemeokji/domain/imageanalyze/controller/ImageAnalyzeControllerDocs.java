package com.mohemeokji.mohemeokji.domain.imageanalyze.controller;

import com.mohemeokji.mohemeokji.domain.imageanalyze.dto.ImageAnalyzeResDto;
import com.mohemeokji.mohemeokji.global.response.ApiResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Image Analyze", description = "이미지 식재료 분석 API")
public interface ImageAnalyzeControllerDocs {

    @Operation(
            summary = "이미지 식재료 분석",
            description = "업로드한 이미지에서 식재료를 자동으로 감지합니다. (지원 형식: jpeg, png, webp, heic / 최대 10MB)"
    )
    @ApiResponses({
            @io.swagger.v3.oas.annotations.responses.ApiResponse(
                    responseCode = "200",
                    description = "분석 성공",
                    content = @Content(schema = @Schema(implementation = ImageAnalyzeResDto.class))
            ),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "400", description = "잘못된 이미지 요청"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "422", description = "식재료 감지 불가"),
            @io.swagger.v3.oas.annotations.responses.ApiResponse(responseCode = "502", description = "AI 분석 서비스 오류")
    })
    ApiResponse<ImageAnalyzeResDto> analyzeImage(
            @Parameter(description = "분석할 이미지 파일", required = true)
            MultipartFile image
    );
}