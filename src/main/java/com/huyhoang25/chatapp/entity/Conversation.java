package com.huyhoang25.chatapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.huyhoang25.chatapp.common.ConversationType;

@Entity
@Table(name = "conversations")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
public class Conversation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String conversationAvatar;

    private ConversationType conversationType;

    @Column(name = "participant_hash", unique = true)
    private String participantHash;

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ChatMessage> message = new ArrayList<>();

    @OneToMany(mappedBy = "conversation", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @Builder.Default
    private List<ConversationParticipant> participants = new ArrayList<>();

    private LocalDateTime createdAt;

    private String lastMessageId;

    private String lastMessageContent;

    private LocalDateTime lastMessageTime;

    public void addParticipants(User user) {
        this.participants.add(ConversationParticipant.builder()
                            .conversation(this) // Set relationship với conversation
                            .user(user) // Set relationship với user
                            .build());
    }

}
