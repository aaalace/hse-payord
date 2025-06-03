package com.aaalace.orderservice.domain.config;

import com.aaalace.orderservice.presentation.socket.order.OrderHandshakeInterceptor;
import com.aaalace.orderservice.presentation.socket.order.OrderSocketHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.socket.config.annotation.EnableWebSocket;
import org.springframework.web.socket.config.annotation.WebSocketConfigurer;
import org.springframework.web.socket.config.annotation.WebSocketHandlerRegistry;

@Configuration
@EnableWebSocket
@RequiredArgsConstructor
public class WebSocketConfig implements WebSocketConfigurer {

    private final OrderSocketHandler orderHandler;
    private final OrderHandshakeInterceptor orderHandshakeInterceptor;

    @Override
    public void registerWebSocketHandlers(WebSocketHandlerRegistry registry) {
        registry.addHandler(orderHandler, "/ws/order")
                .addInterceptors(orderHandshakeInterceptor)
                .setAllowedOrigins("*");
    }
}
