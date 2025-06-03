package com.aaalace.paymentservice.application.scheduler;

import com.aaalace.paymentservice.application.processor.BalanceUpdateCommandProcessor;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class BalanceUpdateCommandScheduler {

    private final BalanceUpdateCommandProcessor balanceUpdateCommandProcessor;

    @Scheduled(fixedDelayString = "2000")
    public void updateBalances() {
        balanceUpdateCommandProcessor.processAtMost50();
    }
}