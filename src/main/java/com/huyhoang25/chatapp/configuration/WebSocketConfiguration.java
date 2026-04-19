package com.huyhoang25.chatapp.configuration;

// import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.ChannelRegistration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSocketMessageBroker
@RequiredArgsConstructor
public class WebSocketConfiguration implements WebSocketMessageBrokerConfigurer {

    // @Value("${app.frontend_url:http://localhost:3000}")
    // private String frontendUrl;

    private final ClientInboundAuthentication clientInboundAuthentication;
    private final WebsocketHandShake websocketHandShake;


    @Override
    public void registerStompEndpoints(StompEndpointRegistry registry) {
        // Configure STOMP endpoint: ws://localhost:8080/ws
        // Allow frontend origin và add handshake interceptor
        registry.addEndpoint("/ws")
        .setAllowedOriginPatterns("*")
        .addInterceptors(websocketHandShake);
    }

    @Bean
    public TaskScheduler heartbeatScheduler() {
        return new ThreadPoolTaskScheduler();
    }

    @Override
    public void configureMessageBroker(MessageBrokerRegistry registry) {
        // Enable in-memory message broker
        // /topic: broadcast messages (1-N), /queue: point-to-point (1-1)
        registry.enableSimpleBroker("/topic", "/queue")
                .setHeartbeatValue(new long[] {10000,10000})
                .setTaskScheduler(heartbeatScheduler());
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
