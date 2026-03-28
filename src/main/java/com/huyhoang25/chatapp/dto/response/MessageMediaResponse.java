package com.huyhoang25.chatapp.dto.response;

import java.time.LocalDateTime;

import lombok.Builder;

@Builder
public record MessageMediaResponse(
    String fileName, // Tên file
    String fileType, // Loại file
    String thumbnailUrl, // URL của file
    LocalDateTime uploadedAt // Thời gian upload
) {
} 