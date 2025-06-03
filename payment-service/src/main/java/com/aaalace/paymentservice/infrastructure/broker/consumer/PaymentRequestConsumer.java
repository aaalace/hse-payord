package com.aaalace.paymentservice.infrastructure.broker.consumer;

import com.aaalace.paymentservice.application.service.OrderService;
import com.aaalace.paymentservice.domain.config.RabbitConfig;
import com.aaalace.paymentservice.domain.dto.PaymentRequestMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRequestConsumer {

    private final OrderService orderService;

    @RabbitListener(queues = RabbitConfig.PAYMENT_KEY)
    public void receivePayment(PaymentRequestMessage message) {
        log.info("Received PaymentRequestMessage: {}", message);
        orderService.processOrder(message);
    }
}
