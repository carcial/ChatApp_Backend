package com.example.chat_app_backend.chats.configs;


import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.SessionConnectedEvent;
import org.springframework.web.socket.messaging.SessionDisconnectEvent;

import java.util.Objects;

@Component
@AllArgsConstructor
public class WebSocketEventListener {

    private static final Logger logger = LoggerFactory.getLogger(WebSocketEventListener.class);

    private SimpMessageSendingOperations messagingTemplate;

    @EventListener
    public void handleWebSocketConnectListener(SessionConnectedEvent event) {
        logger.info("Received a new web socket connection...");
    }

    @EventListener
    public void handleWebSocketDisconnectLister(SessionDisconnectEvent event) {
        StompHeaderAccessor headerAccessor = StompHeaderAccessor.wrap(event.getMessage());

        String username = (String) Objects.requireNonNull(headerAccessor.getSessionAttributes()).get("username");
        if(username !=null) {
            logger.info("User Disconnected:"+username);
            String chatMessage=username +" is Offline....";
            messagingTemplate.convertAndSend("/start/initial."+username, chatMessage);
        }
    }
}
