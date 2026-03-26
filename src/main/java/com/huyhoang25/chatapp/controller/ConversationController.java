package com.huyhoang25.chatapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huyhoang25.chatapp.dto.request.CreateConversationRequest;
import com.huyhoang25.chatapp.dto.response.ApiResponse;
import com.huyhoang25.chatapp.dto.response.ConversationDetailResponse;
import com.huyhoang25.chatapp.dto.response.CreateConversationResponse;
import com.huyhoang25.chatapp.dto.response.PageResponse;
import com.huyhoang25.chatapp.service.ConversationService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/conversations")
public class ConversationController {
    private final ConversationService conversationService;

    @PostMapping
    ApiResponse<CreateConversationResponse> createConversation(
        @AuthenticationPrincipal Jwt jwt, // Lấy thông tin user từ JWT token
        @RequestBody @Valid CreateConversationRequest request
    ) {
        // Lấy userId từ JWT subject (đã được set khi generate token)
        var creatorId = jwt.getSubject();

        var data = conversationService.createConversation(creatorId, request);

        return ApiResponse.<CreateConversationResponse>builder()
        .code(HttpStatus.OK.value())
        .message("Conversation created successfully!")
        .data(data)
        .build();
    }

    ApiResponse<PageResponse<ConversationDetailResponse>> getMyConversation(
        @AuthenticationPrincipal Jwt jwt,
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(required = false, defaultValue = "10") int size
    ) {
        var userId = jwt.getSubject();
        var data = conversationService.getMyConversation(userId, page, size);

        return ApiResponse.<PageResponse<ConversationDetailResponse>>builder()
        .code(HttpStatus.OK.value())
        .message("My conversation retrieved successfully!")
        .data(data)
        .build();
    }
}
