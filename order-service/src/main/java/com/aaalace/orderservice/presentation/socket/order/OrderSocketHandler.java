package com.aaalace.orderservice.presentation.socket.order;

import com.aaalace.orderservice.application.service.OrderService;
import com.aaalace.orderservice.domain.dto.OrderRequestDTO;
import com.aaalace.orderservice.infrastructure.in_memory.UserSessionStorage;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Slf4j
@Component
@RequiredArgsConstructor
public class OrderSocketHandler extends TextWebSocketHandler {

    private final UserSessionStorage storage;
    private final OrderService orderService;

    @Override
    public void afterConnectionEstablished(WebSocketSession session) {
        String userId = (String) session.getAttributes().get("userId");
        storage.register(userId, session);
        log.info("WS /order connection established, userId={}", userId);
    }

    @Override
    public void handleTextMessage(WebSocketSession session, @NonNull TextMessage message) {
        String userId = (String) session.getAttributes().get("userId");
        log.info("WS /order new message, userId={}: {}", userId, message.getPayload());
        orderService.newOrder(new OrderRequestDTO()); // todo: build OrderRequestDTO
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, @NonNull CloseStatus status) {
        String userId = (String) session.getAttributes().get("userId");
        storage.remove(userId);
        log.info("WS /order connection closed, userId={}", userId);
    }
}