package com.aaalace.paymentservice.infrastructure.repository;

import com.aaalace.paymentservice.domain.model.BalanceUpdateCommand;
import com.aaalace.paymentservice.domain.model.CommandState;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface BalanceUpdateCommandRepository extends JpaRepository<BalanceUpdateCommand, UUID> {

    List<BalanceUpdateCommand> findTop50ByStateOrderByIdAsc(CommandState state);
}
