package com.huyhoang25.chatapp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "message_media")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class MessageMedia {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id", nullable = false)
    private ChatMessage message;

    @Column(nullable = false)
    private String filename;

    @Column(nullable = false)
    private String thumbnailUrl;
    
    @Column(nullable = false)
    private String fileType;
    
    @Column(name = "uploaded_at", nullable = false)
    @Builder.Default
    private LocalDateTime uploadedAt = LocalDateTime.now();
}
