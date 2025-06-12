package com.aaalace.orderservice.application.scheduler;

import com.aaalace.orderservice.application.processor.PaymentRequestCommandProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentRequestCommandScheduler {

    private final PaymentRequestCommandProcessor paymentRequestCommandProcessor;

    @Scheduled(fixedDelayString = "2000")
    public void processPayments() {
        paymentRequestCommandProcessor.processAtMost50();
    }
}
