package com.huyhoang25.chatapp.dto.event;

import lombok.Builder;

@Builder
public record PresenceEvent(
        String userId,
        Boolean isOnline,
        String lastOnlineAt) {
}
