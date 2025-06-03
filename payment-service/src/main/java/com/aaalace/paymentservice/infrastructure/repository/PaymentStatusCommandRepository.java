package com.aaalace.paymentservice.infrastructure.repository;

import com.aaalace.paymentservice.domain.model.CommandState;
import com.aaalace.paymentservice.domain.model.PaymentStatusCommand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface PaymentStatusCommandRepository extends JpaRepository<PaymentStatusCommand, UUID> {

    Optional<PaymentStatusCommand> findByBalanceUpdateCommandId(UUID id);

    List<PaymentStatusCommand> findTop50ByStateOrderByIdAsc(CommandState state);
}
