package com.aaalace.orderservice.application.service;

import com.aaalace.orderservice.domain.dto.BalanceUpdateStatus;
import com.aaalace.orderservice.domain.dto.OrderRequestDTO;
import com.aaalace.orderservice.domain.dto.OrderStatusDTO;
import com.aaalace.orderservice.domain.dto.PaymentStatusMessage;
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

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRequestCommandRepository commandRepository;
    private final OrderSocketProducer orderSocketProducer;

    @Transactional
    public void newOrder(@NonNull OrderRequestDTO request) {
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

        OrderStatusDTO statusDTO = OrderStatusDTO.builder()
                .orderId(order.getId())
                .status(message.getStatus())
                .build();
        orderSocketProducer.sendToUser(order.getUserId(), statusDTO);
    }
}
