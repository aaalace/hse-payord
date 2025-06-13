package com.aaalace.paymentservice.application.service;

import com.aaalace.paymentservice.domain.dto.PaymentRequestMessage;
import com.aaalace.paymentservice.domain.model.BalanceUpdateCommand;
import com.aaalace.paymentservice.domain.model.BalanceUpdateStatus;
import com.aaalace.paymentservice.domain.model.PaymentStatusCommand;
import com.aaalace.paymentservice.infrastructure.repository.BalanceUpdateCommandRepository;
import com.aaalace.paymentservice.infrastructure.repository.PaymentStatusCommandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.UUID;

@Slf4j
@Service
@RequiredArgsConstructor
public class OrderService {

    private final BalanceService balanceService;
    private final PaymentStatusCommandRepository paymentStatusCommandRepository;

    @Transactional(rollbackOn = Exception.class)
    public void processOrder(PaymentRequestMessage message) {
        BalanceUpdateCommand ref = balanceService.withdraw(message);

        PaymentStatusCommand command = PaymentStatusCommand.builder()
                .userId(message.getUserId())
                .orderId(message.getOrderId())
                .balanceUpdateCommandId(ref.getId())
                .build();
        paymentStatusCommandRepository.save(command);
        log.info("New payment status command: {}", command);
    }

    public void onRefBalanceUpdateCommand(
            UUID refBalanceUpdateCommandId,
            BalanceUpdateStatus status
    ) {
        PaymentStatusCommand command = paymentStatusCommandRepository
                .findByBalanceUpdateCommandId(refBalanceUpdateCommandId)
                .orElse(null);
        if (command != null) {
            command.setPaymentStatus(status);
            paymentStatusCommandRepository.save(command);
        }
    }

    public boolean isPaymentInSchedule(PaymentRequestMessage message) {
        return paymentStatusCommandRepository.existsByOrderIdAndUserId(message.getOrderId(), message.getUserId());
    }
}
