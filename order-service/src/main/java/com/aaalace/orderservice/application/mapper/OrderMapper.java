package com.aaalace.orderservice.application.mapper;

import com.aaalace.orderservice.domain.dto.BalanceUpdateStatus;
import com.aaalace.orderservice.domain.dto.OrderDTO;
import com.aaalace.orderservice.domain.dto.OrderStatusDTO;
import com.aaalace.orderservice.domain.model.Order;
import com.aaalace.orderservice.domain.model.OrderStatus;

import java.util.List;

public class OrderMapper {

    public static List<OrderDTO> toOrderDTOList(List<Order> orders) {
        return orders.stream().map(OrderMapper::toOrderDTO).toList();
    }

    public static OrderDTO toOrderDTO(Order order) {
        return OrderDTO.builder()
                .id(order.getId())
                .amount(order.getAmount())
                .status(mapStatus(order.getStatus()))
                .build();
    }

    public static OrderStatusDTO toOrderStatusDTO(Order order) {
        return OrderStatusDTO.builder()
                .orderId(order.getId())
                .status(mapStatus(order.getStatus()))
                .build();
    }

    private static BalanceUpdateStatus mapStatus(OrderStatus status) {

        return status == OrderStatus.SUCCESS
                ? BalanceUpdateStatus.SUCCESS
                : status == OrderStatus.PENDING
                ? BalanceUpdateStatus.PENDING
                : BalanceUpdateStatus.FAILED;
    }
}
