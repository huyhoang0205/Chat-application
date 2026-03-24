package com.huyhoang25.chatapp.entity;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "conservations")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class Conservation {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    private String name;

    private String conservationAvatar;

    @Column(name = "participant_hash", unique = true)
    private String participantHash;

    @OneToMany(mappedBy = "conservation", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ChatMessage> message = new ArrayList<>();

    @OneToMany(mappedBy = "conservation", cascade = CascadeType.ALL , fetch = FetchType.LAZY)
    @Builder.Default
    private List<ConservationParticipant> participants = new ArrayList<>();

    private LocalDateTime createdAt;

    private String lastMessageId;

    private String lastMessageContent;

    private LocalDateTime lastMessageTime;

}
