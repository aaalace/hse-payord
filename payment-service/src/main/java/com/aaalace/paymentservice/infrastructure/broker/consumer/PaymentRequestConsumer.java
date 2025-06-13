package com.aaalace.paymentservice.infrastructure.broker.consumer;

import com.aaalace.paymentservice.application.service.OrderService;
import com.aaalace.paymentservice.domain.config.RabbitConfig;
import com.aaalace.paymentservice.domain.dto.PaymentRequestMessage;
import com.aaalace.paymentservice.domain.exception.BadRequestError;
import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.support.AmqpHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRequestConsumer {

    private final OrderService orderService;

    @RabbitListener(queues = RabbitConfig.PAYMENT_KEY, ackMode = "MANUAL")
    public void receivePayment(
            PaymentRequestMessage message,
            Channel channel,
            @Header(AmqpHeaders.DELIVERY_TAG) long tag
    ) throws IOException {
        boolean inSchedule = orderService.isPaymentInSchedule(message);
        if (inSchedule) {
            log.info("Order {} for user {} already processed - skip", message.getOrderId(), message.getUserId());
            channel.basicAck(tag, false);
            return;
        }

        try {
            log.info("Received PaymentRequestMessage: {}", message);
            orderService.processOrder(message);
            channel.basicAck(tag, false);
        } catch (BadRequestError e) {
            log.warn("Processed PaymentRequestMessage with BadRequest state: {}", message);
            channel.basicAck(tag, false);
        } catch (Exception e) {
            log.error("Error processing PaymentRequestMessage: {}", message, e);
            channel.basicNack(tag, false, true);
        }
    }
}
