package com.aaalace.orderservice.infrastructure.broker.consumer;

import com.aaalace.orderservice.application.service.OrderService;
import com.aaalace.orderservice.domain.config.RabbitConfig;
import com.aaalace.orderservice.domain.dto.PaymentStatusMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentStatusConsumer {

    private final OrderService orderService;

    @RabbitListener(queues = RabbitConfig.PAYMENT_STATUS_KEY)
    public void receivePayment(PaymentStatusMessage message) {
        log.info("Received payment status: {}", message);
        orderService.processOrderAfterPayment(message);
    }
}
