package com.huyhoang25.chatapp.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.huyhoang25.chatapp.common.ConversationType;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class CreateConversationResponse {
    private String id;
    private String name;
    private String conversationAvatar;
    private ConversationType conversationType;
    private List<ParticipantResponse> participantInfo;
    private LocalDateTime createdAt;
}
