package com.huyhoang25.chatapp.dto.response;

import lombok.Builder;

@Builder
public record StatusResponse(
    String userId,
    Boolean isOnline,
    String lastOnlineAt
) {
}