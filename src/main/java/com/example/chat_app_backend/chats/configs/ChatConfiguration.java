package com.example.chat_app_backend.chats.configs;

import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.EnableWebSocketMessageBroker;
import org.springframework.web.socket.config.annotation.StompEndpointRegistry;
import org.springframework.web.socket.config.annotation.WebSocketMessageBrokerConfigurer;

@Configuration
@EnableWebSocket
@EnableWebSocketMessageBroker
public class ChatConfiguration implements WebSocketMessageBrokerConfigurer {

    @Override
    public void configureMessageBroker(MessageBrokerRegistry config) {
        config.enableSimpleBroker("/start", "/user");  // Added /user for private messaging
        config.setApplicationDestinationPrefixes("/current");
        //config.setUserDestinationPrefix("/user");
    }

    public void registerStompEndpoints(StompEndpointRegistry registry) {
        registry
                .addEndpoint("/ws")
                .setAllowedOrigins("*");
    }
}
