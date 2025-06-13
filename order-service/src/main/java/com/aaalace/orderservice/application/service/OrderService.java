package com.aaalace.orderservice.application.service;

import com.aaalace.orderservice.application.mapper.OrderMapper;
import com.aaalace.orderservice.domain.dto.*;
import com.aaalace.orderservice.domain.exception.BadRequestError;
import com.aaalace.orderservice.domain.model.Order;
import com.aaalace.orderservice.domain.model.OrderStatus;
import com.aaalace.orderservice.domain.model.PaymentRequestCommand;
import com.aaalace.orderservice.infrastructure.repository.OrderRepository;
import com.aaalace.orderservice.infrastructure.repository.PaymentRequestCommandRepository;
import com.aaalace.orderservice.presentation.socket.order.OrderSocketProducer;
import jakarta.transaction.Transactional;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRequestCommandRepository commandRepository;
    private final OrderSocketProducer orderSocketProducer;

    public List<OrderDTO> getUserOrders(@NonNull String userId) {
        List<Order> orders =  orderRepository.findByUserId(userId);
        return OrderMapper.toOrderDTOList(orders);
    }

    public OrderStatusDTO getOrderStatus(@NonNull String orderId) {
        Order order = orderRepository.findById(orderId).orElse(null);
        if (order == null) {
            log.error("Order with id {} not found", orderId);
            throw new BadRequestError("Order not found");
        }
        return OrderMapper.toOrderStatusDTO(order);
    }

    @Transactional
    public Order newOrder(@NonNull OrderRequestDTO request) {
        Order newOrder = Order.builder()
                .userId(request.getUserId())
                .amount(request.getPrice())
                .build();
        orderRepository.save(newOrder);
        log.info("New Order created: {}", newOrder);

        PaymentRequestCommand command = PaymentRequestCommand.builder()
                .orderId(newOrder.getId())
                .userId(request.getUserId())
                .amount(request.getPrice())
                .build();
        commandRepository.save(command);
        log.info("New PaymentRequestCommand created: {}", newOrder);

        return newOrder;
    }

    public void processOrderAfterPayment(PaymentStatusMessage message) {
        Order order = orderRepository.findById(message.getOrderId()).orElse(null);
        if (order == null) {
            log.error("Order not found id: {}", message.getOrderId());
            throw new BadRequestError("Order not found");
        }

        if (message.getStatus() != BalanceUpdateStatus.SUCCESS) {
            order.setStatus(OrderStatus.FAILED);
        } else {
            order.setStatus(OrderStatus.SUCCESS);
        }
        orderRepository.save(order);

        OrderDTO orderDTO = OrderMapper.toOrderDTO(order);
        orderSocketProducer.sendToUser(order.getUserId(), orderDTO);
    }
}
