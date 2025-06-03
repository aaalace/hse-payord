package com.aaalace.paymentservice.application.processor;

import com.aaalace.paymentservice.application.service.BalanceService;
import com.aaalace.paymentservice.application.service.OrderService;
import com.aaalace.paymentservice.domain.model.BalanceUpdateCommand;
import com.aaalace.paymentservice.domain.model.CommandState;
import com.aaalace.paymentservice.domain.model.BalanceUpdateStatus;
import com.aaalace.paymentservice.infrastructure.repository.BalanceUpdateCommandRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class BalanceUpdateCommandProcessor {

    private final BalanceService balanceService;
    private final OrderService orderService;
    private final BalanceUpdateCommandRepository balanceUpdateCommandRepository;
    private final ApplicationContext context;

    public void processAtMost50() {
        List<BalanceUpdateCommand> commands = balanceUpdateCommandRepository.findTop50ByStateOrderByIdAsc(
                CommandState.PENDING
        );

        BalanceUpdateCommandProcessor self = context.getBean(BalanceUpdateCommandProcessor.class);
        log.info("Start to process {} BalanceUpdateCommand commands", commands.size());
        for (BalanceUpdateCommand command : commands) {
            self.process(command);
        }
    }

    @Transactional
    protected void process(BalanceUpdateCommand command) {
        try {
            BalanceUpdateStatus status = balanceService.update(command.getUserId(), command.getAmount());
            orderService.onRefBalanceUpdateCommand(command.getId(), status);
            command.setState(CommandState.SUCCESS);
            log.info("Processed payment BalanceUpdateCommandId={}, status={}", command.getId(), status);
        } catch (Exception e) {
            command.setState(CommandState.FAILED);
        }
        balanceUpdateCommandRepository.save(command);
    }
}
