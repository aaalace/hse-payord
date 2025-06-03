package com.aaalace.orderservice.infrastructure.broker.producer;

import com.aaalace.orderservice.domain.config.RabbitConfig;
import com.aaalace.orderservice.domain.dto.PaymentRequestMessage;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Component
public class PaymentRequestProducer {

    private final RabbitTemplate rabbitTemplate;

    public PaymentRequestProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(PaymentRequestMessage message) {
        rabbitTemplate.convertAndSend("", RabbitConfig.PAYMENT_KEY, message);
    }
}
