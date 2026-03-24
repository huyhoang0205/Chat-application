package com.huyhoang25.chatapp.entity;

import java.time.LocalDateTime;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "conversation_participants" , uniqueConstraints = @UniqueConstraint(columnNames = {"conservation_id", "user_id"}))
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Getter
@Setter
public class ConservationParticipant {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "conservation_id", nullable = false)
    private Conservation conservation;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "join_at", nullable = false)
    @Builder.Default
    private LocalDateTime joinedAt = LocalDateTime.now();
}
