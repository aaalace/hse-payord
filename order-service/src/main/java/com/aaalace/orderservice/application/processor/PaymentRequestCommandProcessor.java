package com.aaalace.orderservice.application.processor;

import com.aaalace.orderservice.domain.dto.PaymentRequestMessage;
import com.aaalace.orderservice.domain.model.CommandState;
import com.aaalace.orderservice.domain.model.PaymentRequestCommand;
import com.aaalace.orderservice.infrastructure.broker.producer.PaymentRequestProducer;
import com.aaalace.orderservice.infrastructure.repository.PaymentRequestCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class PaymentRequestCommandProcessor {

    private final PaymentRequestCommandRepository paymentRequestCommandRepository;
    private final PaymentRequestProducer paymentRequestProducer;
    private final ApplicationContext context;

    public void processAtMost50() {
        List<PaymentRequestCommand> commands = paymentRequestCommandRepository.findTop50ByStateOrderByIdAsc(
                CommandState.PENDING
        );
        if (commands.isEmpty()) return;

        PaymentRequestCommandProcessor self = context.getBean(PaymentRequestCommandProcessor.class);
        log.info("Start to process {} PaymentRequestCommand commands", commands.size());
        for (PaymentRequestCommand command : commands) {
            self.process(command);
        }
    }

    protected void process(PaymentRequestCommand command) {
        try {
            PaymentRequestMessage message = PaymentRequestMessage.builder()
                    .orderId(command.getOrderId())
                    .userId(command.getUserId())
                    .amount(command.getAmount())
                    .build();
            paymentRequestProducer.sendMessage(message);
            command.setState(CommandState.SUCCESS);
            log.info("Processed PaymentRequestCommand id={}, status={}", command.getId(), command.getState());
        } catch (Exception e) {
            command.setState(CommandState.FAILED);
        }
        paymentRequestCommandRepository.save(command);
    }
}
