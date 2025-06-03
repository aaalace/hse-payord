package com.aaalace.paymentservice.application.scheduler;

import com.aaalace.paymentservice.application.processor.PaymentStatusCommandProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentStatusCommandScheduler {

    private final PaymentStatusCommandProcessor paymentStatusCommandProcessor;

    @Scheduled(fixedDelayString = "2000")
    public void broadcastPaymentStatuses() {
        paymentStatusCommandProcessor.processAtMost50();
    }
}
