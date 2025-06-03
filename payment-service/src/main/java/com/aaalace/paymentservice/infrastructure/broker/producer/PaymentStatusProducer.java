package com.aaalace.paymentservice.infrastructure.broker.producer;

import com.aaalace.paymentservice.domain.config.RabbitConfig;
import com.aaalace.paymentservice.domain.dto.PaymentStatusMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class PaymentStatusProducer {

    private final RabbitTemplate rabbitTemplate;

    public PaymentStatusProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(PaymentStatusMessage message) {
        rabbitTemplate.convertAndSend("", RabbitConfig.PAYMENT_STATUS_KEY, message);
        log.info("Sent PaymentStatusMessage: {}", message);
    }
}
