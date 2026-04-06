package com.huyhoang25.chatapp.mapper;


import java.time.Duration;
import java.time.Instant;

import org.springframework.stereotype.Component;

import com.huyhoang25.chatapp.common.ConversationType;
import com.huyhoang25.chatapp.dto.response.ConversationDetailResponse;
import com.huyhoang25.chatapp.dto.response.CreateConversationResponse;
import com.huyhoang25.chatapp.dto.response.ParticipantResponse;
import com.huyhoang25.chatapp.entity.Conversation;
import com.huyhoang25.chatapp.service.UserSessionService;

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

        if(conversationType == ConversationType.PRIVATE) {
            // Với PRIVATE: tên là username của người còn lại (không phải creator)
            conversation.getParticipants()
                .stream()
                .filter(participant -> !participant.getUser().getId().equals(creatorId))
                .findFirst()
                .ifPresent(participantInfo -> response.setName(participantInfo.getUser().getUsername()));
        } else {
            // Với GROUP: dùng tên nhóm và avatar từ conversation
            response.setName(conversation.getName());
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
        String name = resolveConversationName(creatorId, conversation);
        response.setName(name);

        if( conversation.getConversationType() != ConversationType.PRIVATE) {
            response.setConversationAvatar(conversation.getConversationAvatar());
        }

        if(conversationType == ConversationType.PRIVATE) {
            // Private conversation: Check other user's online status
            conversation.getParticipants().stream()
                    .filter(p -> !p.getUser().getId().equals(creatorId))
                    .findFirst()
                    .ifPresent(p -> {
                        String otherUserId = p.getUser().getId();
                        boolean isOnline = userSessionService.isOnline(p.getUser().getId());
                        String lastOnlineAt = userSessionService.getPresence(otherUserId)
                                    .map(presence -> formatLastOnlineAt(presence.getLastOnlineAt()))
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

    public String formatLastOnlineAt(Instant lastOnlineAt) {
        if(lastOnlineAt == null) return null;

        long minutes = Duration.between(lastOnlineAt, Instant.now()).toMinutes();

        if(minutes < 1) return "Vừa hoạt động xong";
        if (minutes < 60)   return "Hoạt động " + minutes + " phút trước";
        if (minutes < 1440) return "Hoạt động " + (minutes / 60) + " giờ trước";
        return "Hoạt động " + (minutes / 1440) + " ngày trước";
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
