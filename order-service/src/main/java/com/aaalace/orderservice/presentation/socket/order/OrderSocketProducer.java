package com.aaalace.orderservice.presentation.socket.order;

import com.aaalace.orderservice.domain.dto.OrderDTO;
import com.aaalace.orderservice.domain.dto.OrderStatusDTO;
import com.aaalace.orderservice.infrastructure.in_memory.UserSessionStorage;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSocketProducer {

    private final UserSessionStorage sessionStorage;
    private final ObjectMapper objectMapper;

    public void sendToUser(String userId, OrderDTO dto) {
        WebSocketSession session = sessionStorage.get(userId);

        if (session == null) {
            log.warn("No WebSocket session for userId: {}", userId);
            return;
        }
        if (!session.isOpen()) {
            log.warn("WebSocket session is closed for userId: {}", userId);
            return;
        }

        try {
            String json = objectMapper.writeValueAsString(dto);
            session.sendMessage(new TextMessage(json));
            log.info("Sent message to {}: {}", userId, dto);
        } catch (Exception e) {
            log.error("Failed to send message to userId: {}", userId, e);
        }
    }
}