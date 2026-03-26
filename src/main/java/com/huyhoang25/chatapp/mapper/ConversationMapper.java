package com.huyhoang25.chatapp.mapper;


import com.huyhoang25.chatapp.common.ConversationType;
import com.huyhoang25.chatapp.dto.response.ConversationDetailResponse;
import com.huyhoang25.chatapp.dto.response.CreateConversationResponse;
import com.huyhoang25.chatapp.dto.response.ParticipantResponse;
import com.huyhoang25.chatapp.entity.Conversation;

public final class ConversationMapper {
    private ConversationMapper() {

    }
    public static CreateConversationResponse toConversationResponse(String creatorId, Conversation conversation) {
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

    public static ConversationDetailResponse tConversationDetailResponse(String creatorId, Conversation conversation) {
        ConversationType conversationType = conversation.getConversationType();

        ConversationDetailResponse response = ConversationDetailResponse.builder()
        .id(conversation.getId())
        .conversationType(conversationType)
        .participantInfo(conversation.getParticipants().stream()
                                        .map(participant -> ParticipantResponse.builder()
                                                                    .userId(participant.getId())
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
        return response;
    }

    private static String resolveConversationName(String creatorId, Conversation conversation) {
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
