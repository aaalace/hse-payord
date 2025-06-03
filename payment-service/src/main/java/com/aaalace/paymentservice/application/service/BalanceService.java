package com.aaalace.paymentservice.application.service;

import com.aaalace.paymentservice.domain.dto.DepositRequest;
import com.aaalace.paymentservice.application.mapper.BalanceMapper;
import com.aaalace.paymentservice.domain.dto.PaymentRequestMessage;
import com.aaalace.paymentservice.domain.exception.BadRequestError;
import com.aaalace.paymentservice.domain.exception.InternalServerError;
import com.aaalace.paymentservice.domain.model.*;
import com.aaalace.paymentservice.infrastructure.repository.BalanceRepository;
import com.aaalace.paymentservice.domain.dto.BalanceDTO;
import com.aaalace.paymentservice.infrastructure.repository.BalanceUpdateCommandRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
public class BalanceService {

    private final BalanceRepository balanceRepository;
    private final BalanceUpdateCommandRepository balanceUpdateCommandRepository;

    public BalanceDTO getByUserId(String userId) {
        Balance balance = balanceRepository.findByUserId(userId).orElse(null);
        if (balance == null) {
            log.warn("Balance not found, userId={}", userId);
            throw new BadRequestError("Balance not found");
        }

        return BalanceMapper.mapToBalanceDTO(balance);
    }

    public BalanceDTO open(String userId) {
        Balance balance = balanceRepository.findByUserId(userId).orElse(null);
        if (balance != null) {
            log.warn("Balance for user already exists, userId={}", userId);
            throw new BadRequestError("Balance for user already exists");
        }

        Balance newBalance = Balance.builder()
                .userId(userId)
                .balance(BigDecimal.ZERO)
                .build();
        balanceRepository.save(newBalance);
        log.info("New balance account for userId={}", userId);

        return BalanceMapper.mapToBalanceDTO(newBalance);
    }

    public void deposit(DepositRequest payload) {
        log.info("Deposit payload={}", payload);
        if (payload.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            log.warn("Deposit is less or equals to zero, userId={}", payload.getUserId());
            throw new BadRequestError("Deposit is less or equals to zero");
        }

        BalanceUpdateCommand command = BalanceUpdateCommand.builder()
                .userId(payload.getUserId())
                .amount(payload.getAmount())
                .type(BalanceUpdateCommandType.DEPOSIT)
                .build();
        balanceUpdateCommandRepository.save(command);
        log.info("New deposit command: {}", command);
    }

    public BalanceUpdateCommand withdraw(PaymentRequestMessage message) {
        if (message.getAmount().compareTo(BigDecimal.ZERO) < 0) {
            log.warn("Received payment amount < 0, userId={}", message.getUserId());
            throw new BadRequestError("Withdraw is less than zero");
        }

        BalanceUpdateCommand command = BalanceUpdateCommand.builder()
                .userId(message.getUserId())
                .amount(message.getAmount().negate())
                .type(BalanceUpdateCommandType.WITHDRAWAL)
                .build();
        command = balanceUpdateCommandRepository.save(command);
        log.info("New withdraw command: {}", command);

        return command;
    }

    public BalanceUpdateStatus update(String userId, BigDecimal amount) {
        try {
            BalanceDTO prevBalance;
            try {
                prevBalance = getByUserId(userId);
            } catch (BadRequestError e) {
                log.warn("Balance not exists, userId={}", userId);
                return BalanceUpdateStatus.BALANCE_NOT_EXISTS;
            }

            if (prevBalance.getBalance().add(amount).compareTo(BigDecimal.ZERO) < 0) {
                log.warn("Insufficient funds, userId={}", userId);
                return BalanceUpdateStatus.INSUFFICIENT_FUNDS;
            }

            balanceRepository.addAmountByUserId(userId, amount);
            log.info("Balance updated for userId={}", userId);

            return BalanceUpdateStatus.SUCCESS;
        } catch (Exception e) {
            log.error("Can not update balance:", e);
            throw new InternalServerError("Can not update balance");
        }
    }
}
