package com.aaalace.orderservice.application.scheduler;

import org.springframework.scheduling.annotation.Scheduled;

public class PaymentStatusCommandScheduler {

    @Scheduled(fixedRate = 2000)
    public void processPaymentStatuses() {
        // todo: call PaymentStatusCommandProcessor
    }
}
