package com.aaalace.orderservice.application.scheduler;

import org.springframework.scheduling.annotation.Scheduled;

public class PaymentRequestCommandScheduler {

    @Scheduled(fixedRate = 2000)
    public void processPayments() {
        // todo: call PaymentRequestCommandProcessor
    }
}
