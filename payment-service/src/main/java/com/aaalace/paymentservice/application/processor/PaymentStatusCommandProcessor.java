package com.aaalace.paymentservice.application.processor;

import com.aaalace.paymentservice.domain.dto.PaymentStatusMessage;
import com.aaalace.paymentservice.domain.model.*;
import com.aaalace.paymentservice.infrastructure.broker.producer.PaymentStatusProducer;
import com.aaalace.paymentservice.infrastructure.repository.PaymentStatusCommandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentStatusCommandProcessor {

    private final PaymentStatusCommandRepository paymentStatusCommandRepository;
    private final PaymentStatusProducer paymentStatusProducer;
    private final ApplicationContext context;

    public void processAtMost50() {
        List<PaymentStatusCommand> commands = paymentStatusCommandRepository.findTop50ByStateOrderByIdAsc(
                CommandState.PENDING
        );
        if (commands.isEmpty()) return;

        PaymentStatusCommandProcessor self = context.getBean(PaymentStatusCommandProcessor.class);
        log.info("Start to process {} PaymentStatusCommand commands", commands.size());
        for (PaymentStatusCommand command : commands) {
            if (command.getPaymentStatus() != BalanceUpdateStatus.PENDING) self.process(command);
        }
    }

    @Transactional
    protected void process(PaymentStatusCommand command) {
        try {
            PaymentStatusMessage message = PaymentStatusMessage.builder()
                    .orderId(command.getOrderId())
                    .userId(command.getUserId())
                    .status(command.getPaymentStatus())
                    .build();
            paymentStatusProducer.sendMessage(message);
            command.setState(CommandState.SUCCESS);
        } catch (Exception e) {
            command.setState(CommandState.FAILED);
        }
        paymentStatusCommandRepository.save(command);
    }
}
