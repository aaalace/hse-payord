package com.aaalace.orderservice.presentation.controller;

import com.aaalace.orderservice.application.service.OrderService;
import com.aaalace.orderservice.domain.dto.OrderDTO;
import com.aaalace.orderservice.domain.dto.OrderStatusDTO;
import com.aaalace.orderservice.domain.generic.GenericJsonResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/v1/order")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/{userId}")
    public GenericJsonResponse<List<OrderDTO>> getUserOrders(@PathVariable String userId) {
        List<OrderDTO> orders = orderService.getUserOrders(userId);
        return GenericJsonResponse.success(orders);
    }

    @GetMapping("/{orderId}/status")
    public GenericJsonResponse<OrderStatusDTO> getOrderStatus(@PathVariable String orderId) {
        OrderStatusDTO orderStatusDTO = orderService.getOrderStatus(orderId);
        return GenericJsonResponse.success(orderStatusDTO);
    }
}
