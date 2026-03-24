package com.huyhoang25.chatapp.entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.huyhoang25.chatapp.common.MessageType;

@Entity
@Table(name = "chat_message")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Setter
@Getter
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conservation_id", nullable = true)
    private Conservation conservation;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Enumerated(EnumType.STRING)
    @Builder.Default
    private MessageType messageType = MessageType.TEXT;

    @Column(name = "sent_at", nullable = false)
    @Builder.Default
    private LocalDateTime sentAt = LocalDateTime.now();

    @OneToMany(mappedBy = "message", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<MessageMedia> mediaFiles = new ArrayList<>();

}
