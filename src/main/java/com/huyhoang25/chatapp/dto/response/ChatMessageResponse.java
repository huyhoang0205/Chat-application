package com.huyhoang25.chatapp.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.huyhoang25.chatapp.common.MessageType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Builder(toBuilder = true)
@JsonInclude(JsonInclude.Include.NON_NULL)
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageResponse {
    private String id; // Message ID từ database
    private String tempId; // Temporary ID từ client
    private String conversationId;
    private String conversationAvatar; // Avatar của conversation (cho GROUP)
    private String senderId;
    private String senderName;
    private String content;
    private MessageType messageType;
    private List<MessageMediaResponse> messageMedia;
    private LocalDateTime createdAt;
}
