package com.huyhoang25.chatapp.dto.response;

import java.time.LocalDateTime;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.huyhoang25.chatapp.common.ConversationType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ConversationDetailResponse {
    private String id;
    private ConversationType conversationType;
    private String name;
    private String conversationAvatar;
    private List<ParticipantResponse> participantInfo;

    private String lastMessageId;
    private String lastMessageContent;
    private LocalDateTime lastMessageTime;

    private LocalDateTime createdAt;

    private Boolean isOnline; 
    private String lastOnlineAt;
}
