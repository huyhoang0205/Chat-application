package com.huyhoang25.chatapp.dto.request;

import java.util.List;

import com.huyhoang25.chatapp.common.ConversationType;

import jakarta.validation.constraints.NotNull;

public record CreateConversationRequest(
    String name, // Tên conversation (bắt buộc với GROUP, không cần với PRIVATE)
    String conversationAvatar, // Avatar của nhóm (optional)

    @NotNull(message = "Conversation type is required")
    ConversationType conversationType, // PRIVATE hoặc GROUP
    
    List<String> participantIds // Danh sách userId của người tham gia
) {
} 