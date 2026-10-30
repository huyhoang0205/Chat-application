package com.huyhoang25.chatapp.mapper;


import org.springframework.stereotype.Component;

import com.huyhoang25.chatapp.common.ConversationType;
import com.huyhoang25.chatapp.dto.response.ConversationDetailResponse;
import com.huyhoang25.chatapp.dto.response.CreateConversationResponse;
import com.huyhoang25.chatapp.dto.response.ParticipantResponse;
import com.huyhoang25.chatapp.entity.Conversation;
import com.huyhoang25.chatapp.service.UserSessionService;
import com.huyhoang25.chatapp.util.DateTimeUtils;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public final class ConversationMapper {

    private final UserSessionService userSessionService;
    
    public CreateConversationResponse toConversationResponse(String creatorId, Conversation conversation) {
        ConversationType conversationType = conversation.getConversationType();

        CreateConversationResponse response = CreateConversationResponse.builder()
        .id(conversation.getId())
        .conversationType(conversationType)
        // Map danh sách participants sang ParticipantResponse
        .participantInfo(conversation.getParticipants().stream()
                                                        .map(participant -> ParticipantResponse.builder()
                                                                                .userId(participant.getId())
                                                                                .username(participant.getUser().getUsername())
                                                                                .build())
                                                        .toList())
        .createdAt(conversation.getCreatedAt())
        .build();

        response.setName(resolveConversationName(creatorId, conversation));

        if (conversationType != ConversationType.PRIVATE) {
            response.setConversationAvatar(conversation.getConversationAvatar());
        }

        return response;
    }

    public ConversationDetailResponse tConversationDetailResponse(String creatorId, Conversation conversation) {
        ConversationType conversationType = conversation.getConversationType();

        ConversationDetailResponse response = ConversationDetailResponse.builder()
        .id(conversation.getId())
        .conversationType(conversationType)
        .participantInfo(conversation.getParticipants().stream()
                                        .map(participant -> ParticipantResponse.builder()
                                                                    .userId(participant.getUser().getId())
                                                                    .username(participant.getUser().getUsername())
                                                                    .build())
                                        .toList())   
        // Thông tin tin nhắn cuối cùng
        .lastMessageId(conversation.getLastMessageId())
        .lastMessageContent(conversation.getLastMessageContent())
        .lastMessageTime(conversation.getLastMessageTime())
        .createdAt(conversation.getCreatedAt())
        .build();   
        
        // Resolve tên conversation
        response.setName(resolveConversationName(creatorId, conversation));

        if(conversationType == ConversationType.PRIVATE) {
            // Private conversation: Check other user's online status
            conversation.getParticipants().stream()
                    .filter(p -> !p.getUser().getId().equals(creatorId))
                    .findFirst()
                    .ifPresent(p -> {
                        String otherUserId = p.getUser().getId();
                        boolean isOnline = userSessionService.isOnline(p.getUser().getId());
                        String lastOnlineAt = userSessionService.getPresence(otherUserId)
                                    .map(presence -> DateTimeUtils.formatLastOnlineAt(presence.getLastOnlineAt()))
                                    .orElse(null);

                        response.setIsOnline(isOnline);
                        response.setLastOnlineAt(lastOnlineAt);
                    });
        } else {
            // Group conversation: Check if any member is online
            boolean anyOnline = conversation.getParticipants().stream()
            .filter(p -> !p.getUser().getId().equals(creatorId))
            .anyMatch(p -> userSessionService.isOnline(p.getUser().getId()));

            response.setIsOnline(anyOnline);
        }

        if(conversationType != ConversationType.PRIVATE) {
            response.setConversationAvatar(conversation.getConversationAvatar());
        }

        return response;
    }

    private String resolveConversationName(String creatorId, Conversation conversation) {
        if(conversation.getConversationType() == ConversationType.PRIVATE) {
            return conversation.getParticipants().stream()
            .filter(p -> !p.getUser().getId().equals(creatorId)) // Lọc người còn lại
            .findFirst()
            .map(p -> p.getUser().getUsername()) // Lấy username
            .orElse(null);
        }
        return conversation.getName();// Trả về tên nhóm
    }
}
