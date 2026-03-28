package com.huyhoang25.chatapp.dto.request;

import java.util.List;

import com.huyhoang25.chatapp.common.MessageType;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ChatMessageRequest(
    String tempId, // Temporary ID từ client để map với message đã gửi (optimistic UI)

    @NotBlank(message = "Conversation id is required")
    String conversationId,

    String content, // Nội dung tin nhắn (bắt buộc với TEXT, optional với MEDIA)

    @NotNull(message = "Message Type is required")
    MessageType messageType, // TEXT hoặc MEDIA

    List<MessageMediaRequest> messageMedia //Danh sách media files (optional)
) {

}
