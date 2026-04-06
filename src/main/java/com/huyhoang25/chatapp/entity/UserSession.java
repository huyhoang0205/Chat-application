package com.huyhoang25.chatapp.entity;

import java.time.Instant;

import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@Builder
public class UserSession {
    private String userId;
    private String sessionId;
    private Instant connectedAt;
}
