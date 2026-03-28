package com.huyhoang25.chatapp.controller;

import org.springframework.http.HttpStatus;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.huyhoang25.chatapp.dto.request.ChatMessageRequest;
import com.huyhoang25.chatapp.dto.response.ApiResponse;
import com.huyhoang25.chatapp.dto.response.ChatMessageResponse;
import com.huyhoang25.chatapp.dto.response.PageResponse;
import com.huyhoang25.chatapp.service.ChatMessageService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/chat-message")
public class ChatMessageController {
    private final ChatMessageService chatMessageService;

    @PostMapping
    ApiResponse<ChatMessageResponse> sendChatMessage(
        @AuthenticationPrincipal Jwt jwt,
        @RequestBody @Valid ChatMessageRequest request
    ){
        var senderId = jwt.getSubject();
        var data = chatMessageService.sendChatMessage(senderId, request);

        return ApiResponse.<ChatMessageResponse>builder()
        .code(HttpStatus.OK.value())
        .message("Chat message sent successfully!")
        .data(data)
        .build();
    }

    @GetMapping("/conversations/{conversationId}/message")
    ApiResponse<PageResponse<ChatMessageResponse>> getMessages(
        @PathVariable String conversationId, // Lấy conversationId từ URL path
        @RequestParam(required = false, defaultValue = "1") int page,
        @RequestParam(required = false, defaultValue = "20") int size
    ){
        var data = chatMessageService.getMessageByConversationId(conversationId, page, size);

        return ApiResponse.<PageResponse<ChatMessageResponse>>builder()
            .code(HttpStatus.OK.value())
            .message("Message retrieved successfully!")
            .data(data)
            .build();
    }
}
