package com.huyhoang25.chatapp.configuration;

import java.security.Principal;

import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import com.huyhoang25.chatapp.dto.response.StatusResponse;
import com.huyhoang25.chatapp.mapper.ConversationMapper;
import com.huyhoang25.chatapp.service.UserSessionService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    
    private final UserSessionService userSessionService;
    private final ConversationMapper conversationMapper;
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
        Boolean status = userSessionService.isOnline(userId);
        if(!status) {
            StatusResponse response = StatusResponse.builder()
                                        .userId(userId)
                                        .isOnline(status)
                                        .lastOnlineAt(conversationMapper.formatLastOnlineAt(null))
                                        .build();
            simpMessagingTemplate.convertAndSend("/topic/status", response);
        }


        log.info("User {} disconnected with session {}", userId, sessionId);
    }
}
