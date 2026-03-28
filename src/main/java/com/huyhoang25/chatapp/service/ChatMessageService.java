package com.huyhoang25.chatapp.service;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.huyhoang25.chatapp.dto.request.ChatMessageRequest;
import com.huyhoang25.chatapp.dto.response.ChatMessageResponse;
import com.huyhoang25.chatapp.dto.response.MessageMediaResponse;
import com.huyhoang25.chatapp.dto.response.PageResponse;
import com.huyhoang25.chatapp.entity.ChatMessage;
import com.huyhoang25.chatapp.entity.Conversation;
import com.huyhoang25.chatapp.entity.MessageMedia;
import com.huyhoang25.chatapp.entity.User;
import com.huyhoang25.chatapp.exception.AppException;
import com.huyhoang25.chatapp.exception.ErrorCode;
import com.huyhoang25.chatapp.repository.ChatMessageRepository;
import com.huyhoang25.chatapp.repository.ConversationRepository;
import com.huyhoang25.chatapp.repository.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class ChatMessageService {

    private final UserRepository userRepository;
    private final ConversationRepository conversationRepository;
    private final ChatMessageRepository chatMessageRepository;

    @Transactional(rollbackFor = Exception.class)
    public ChatMessageResponse sendChatMessage(String senderId, ChatMessageRequest request) {
        // 1. Validate sender tồn tại
        User sender = userRepository.findById(senderId)
            .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        // 2. Validate conversation tồn tại và sender là member
        Conversation conversation =  conversationRepository.findByIdAndMember(request.conversationId(),senderId)
            .orElseThrow(() -> new AppException(ErrorCode.NOT_CONVERSATION_MEMBER));

        // 3. Tạo danh sách media files (nếu có)
        // MessageMedia là POJO, sẽ được serialize thành JSON
        List<MessageMedia> media = request.messageMedia() != null && !request.messageMedia().isEmpty() ?
                        request.messageMedia().stream()
                                .map(messageMedia -> MessageMedia.builder()
                                                        .filename(messageMedia.fileName())
                                                        .fileType(messageMedia.fileType())
                                                        .thumbnailUrl(messageMedia.thumbnailUrl())
                                                        .build())
                                .toList() : List.of();
        // 4. Tạo chat message entity
        ChatMessage message = ChatMessage.builder()
                                .conversation(conversation)
                                .sender(sender)
                                .content(request.content())
                                .messageType(request.messageType())
                                .mediaFiles(media)
                                .build();
        // 5. Lưu message vào database
        // media.forEach(message::addMediaFiles);
        chatMessageRepository.save(message);
        // 6. Update lastMessage của conversation
        conversation.setLastMessageId(senderId);
        conversation.setLastMessageContent(message.getContent());
        conversation.setLastMessageTime(message.getSentAt());
        conversationRepository.save(conversation);
        // 7. Map entity sang response DTO
        return ChatMessageResponse.builder()
        .id(message.getId())
        .tempId(request.tempId())
        .conversationId(message.getConversation().getId())
        .conversationAvatar(message.getConversation().getConversationAvatar())
        .senderId(senderId)
        .senderName(sender.getUsername())
        .content(message.getContent())
        .messageType(message.getMessageType())
        .messageMedia(message.getMediaFiles().stream()
                                    .map(messageMedia -> MessageMediaResponse.builder()
                                                            .fileName(messageMedia.getFilename())
                                                            .fileType(messageMedia.getFileType())
                                                            .thumbnailUrl(messageMedia.getThumbnailUrl())
                                                            .uploadedAt(messageMedia.getUploadedAt())
                                                            .build())
                                                        .toList())
        .createdAt(message.getSentAt())
        .build();
    }


    public PageResponse<ChatMessageResponse> getMessageByConversationId(String conversationId, int page, int size) {
        // 1. Lấy thông tin user từ SecurityContext
        // SecurityContextHolder: Lưu trữ thông tin authentication của request hiện tại
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if(authentication == null) {
            throw new AppException(ErrorCode.UNAUTHORIZED);
        }
        // 2. Lấy userId từ authentication
        String userId = authentication.getName();
        // 3. Validate conversation tồn tại và user là member
        Conversation conversation = conversationRepository.findByIdAndMember(conversationId, userId)
                            .orElseThrow(() -> new AppException(ErrorCode.NOT_CONVERSATION_MEMBER));

        // 4. Tạo Pageable với sort theo sentAt DESC (tin nhắn mới nhất lên đầu)
        Pageable pageable = PageRequest.of(page -1, size, Sort.by(Sort.Direction.DESC,"sentAt"));
        // 5. Query messages từ database với pagination
        Page<ChatMessage> chatMessPage = chatMessageRepository.findByConversationId(conversationId, pageable);
        // 6. Lấy danh sách messages từ Page object
        List<ChatMessage> chatMessages = chatMessPage.getContent();
        // 7. Map từng message entity sang response DTO
        List<ChatMessageResponse> response = chatMessages.stream()
                                                .map(chatMessage -> ChatMessageResponse.builder()
                                                                    .id(chatMessage.getId())
                                                                    .conversationId(conversation.getId())
                                                                    .conversationAvatar(conversation.getConversationAvatar())
                                                                    .senderId(chatMessage.getSender().getId())
                                                                    .senderName(chatMessage.getSender().getUsername())
                                                                    .content(chatMessage.getContent())
                                                                    .messageType(chatMessage.getMessageType())
                                                                    // Map media files
                                                                    .messageMedia(chatMessage.getMediaFiles() != null ?chatMessage.getMediaFiles().stream()
                                                                            .map(messageMedia -> MessageMediaResponse.builder()
                                                                                            .fileName(messageMedia.getFilename())
                                                                                            .fileType(messageMedia.getFileType())
                                                                                            .thumbnailUrl(messageMedia.getThumbnailUrl())
                                                                                            .uploadedAt(messageMedia.getUploadedAt())
                                                                                            .build())
                                                                                        .toList() : List.of())
                                                                    .createdAt(chatMessage.getSentAt())
                                                                    .build())
                                                                .toList();
        return PageResponse.<ChatMessageResponse>builder()
        .currentPage(page)
        .pageSize(pageable.getPageSize())
        .totalPage(chatMessPage.getTotalPages())
        .totalElement(chatMessPage.getTotalElements())
        .content(response)
        .build();
                                                                    
    }
}
