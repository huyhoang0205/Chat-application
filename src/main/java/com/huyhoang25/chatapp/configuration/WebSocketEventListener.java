package com.huyhoang25.chatapp.configuration;

import java.security.Principal;
import java.time.Instant;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.huyhoang25.chatapp.dto.event.PresenceEvent;
import com.huyhoang25.chatapp.service.UserSessionService;
import com.huyhoang25.chatapp.util.DateTimeUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    
    private final UserSessionService userSessionService;
    private final SimpMessagingTemplate simpMessagingTemplate;

    @EventListener
    public void onConnect(SessionConnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());
        Principal user = accessor.getUser();
        if (user == null || user.getName() == null ) {
            return;
        }

        String userId = user.getName();
        String sessionId = accessor.getSessionId();

        userSessionService.saveSession(userId, sessionId);
        log.info("User {} connected with session {}", userId, sessionId);

        PresenceEvent presenceEvent = PresenceEvent.builder()
                .userId(userId)
                .isOnline(true)
                .lastOnlineAt(null)
                .build();

        simpMessagingTemplate.convertAndSend("/topic/presence", presenceEvent);
    }

    @EventListener
    public void onDisconnect(SessionDisconnectEvent event) {
        StompHeaderAccessor accessor = StompHeaderAccessor.wrap(event.getMessage());

        Principal user = accessor.getUser();
        if(user  == null || user.getName() == null) {
            return;
        }
        String userId = user.getName();
        String sessionId = accessor.getSessionId();

        userSessionService.removeSession(userId, sessionId);
        log.info("User {} disconnected with session {}", userId, sessionId);

        Boolean status = userSessionService.isOnline(userId);
        if(!status) {
            PresenceEvent presenceEvent = PresenceEvent.builder()
                    .userId(userId)
                    .isOnline(false)
                    .lastOnlineAt(DateTimeUtils.formatLastOnlineAt(Instant.now()))
                    .build();
            simpMessagingTemplate.convertAndSend(
                    "/topic/presence",
                    presenceEvent
            );
        }
    }
}
