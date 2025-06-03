package com.aaalace.orderservice.infrastructure.in_memory;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
@Scope("singleton")
public class UserSessionStorage {

    private final Map<String, WebSocketSession> sessions = new ConcurrentHashMap<>();

    public WebSocketSession get(String userId) {
        return sessions.get(userId);
    }

    public void register(String userId, WebSocketSession session) {
        sessions.put(userId, session);
    }

    public void remove(String userId) {
        sessions.remove(userId);
    }
}