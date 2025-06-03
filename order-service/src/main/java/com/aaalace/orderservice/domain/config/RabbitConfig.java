package com.aaalace.orderservice.domain.config;

import org.springframework.amqp.core.Queue;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitConfig {

    public static final String PAYMENT_KEY = "payment";

    public static final String PAYMENT_STATUS_KEY = "payment.status";

    @Bean
    public Queue paymentQueue() {
        return new Queue(PAYMENT_KEY, true, false, false);
    }

    @Bean
    public Queue paymentStatusQueue() {
        return new Queue(PAYMENT_STATUS_KEY, true, false, false);
    }

    @Bean
    public MessageConverter jsonMessageConverter() {
        return new Jackson2JsonMessageConverter();
    }
}