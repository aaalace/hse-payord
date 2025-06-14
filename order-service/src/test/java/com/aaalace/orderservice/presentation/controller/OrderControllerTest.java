package com.aaalace.orderservice.presentation.controller;

import com.aaalace.orderservice.application.service.OrderService;
import com.aaalace.orderservice.domain.dto.BalanceUpdateStatus;
import com.aaalace.orderservice.domain.dto.OrderDTO;
import com.aaalace.orderservice.domain.dto.OrderStatusDTO;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@TestConfiguration
class TestConfig {

    @Bean
    public OrderService orderService() {
        return Mockito.mock(OrderService.class);
    }
}

@WebMvcTest(OrderController.class)
@Import(TestConfig.class)
class OrderControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderService orderService;

    @Test
    void testOrderList() throws Exception {
        String orderId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();

        OrderDTO orderDTO = OrderDTO.builder()
                .id(orderId)
                .amount(new BigDecimal("100"))
                .status(BalanceUpdateStatus.PENDING)
                .build();

        List<OrderDTO> orders = Collections.singletonList(orderDTO);
        when(orderService.getUserOrders(userId)).thenReturn(orders);
        
        mockMvc.perform(get("/v1/order/{userId}", userId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data[0].id").value(orderId))
            .andExpect(jsonPath("$.data[0].amount").value(new BigDecimal("100")))
            .andExpect(jsonPath("$.data[0].status").value(BalanceUpdateStatus.PENDING.name()));
    }

    @Test
    void testOrderStatus() throws Exception {
        String orderId = UUID.randomUUID().toString();
        OrderStatusDTO orderStatusDTO = OrderStatusDTO.builder()
                .orderId(orderId)
                .status(BalanceUpdateStatus.PENDING)
                .build();
        when(orderService.getOrderStatus(orderId)).thenReturn(orderStatusDTO);

        mockMvc.perform(get("/v1/order/{orderId}/status", orderId))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.orderId").value(orderId))
            .andExpect(jsonPath("$.data.status").value(BalanceUpdateStatus.PENDING.name()));
    }
} 