package com.huyhoang25.chatapp.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    private final ClientInboundAuthentication clientInboundAuthentication;
    private final WebsocketHandShake websocketHandShake;


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Configure STOMP endpoint: ws://localhost:8080/ws
        // Allow frontend origin và add handshake interceptor
        registry.addEndpoint("/ws")
        .setAllowedOrigins("http://localhost:3000")
        .addInterceptors(websocketHandShake);
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enable in-memory message broker
        // /topic: broadcast messages (1-N), /queue: point-to-point (1-1)
        registry.enableSimpleBroker("/topic", "/queue");
        // Prefix cho messages từ client → server
        // Client gửi đến /app/xxx → @MessageMapping("/xxx") xử lý
        registry.setApplicationDestinationPrefixes("/app");
        // Prefix cho user-specific destinations
        // /user/{username}/queue/xxx → Spring convert thành session-specific destination
        registry.setUserDestinationPrefix("/user");
    }

    @Override
    public void configureClientInboundChannel(ChannelRegistration registration) {
        // Register ChannelInterceptor để authenticate STOMP CONNECT frames
        registration.interceptors(clientInboundAuthentication);
    }

}
