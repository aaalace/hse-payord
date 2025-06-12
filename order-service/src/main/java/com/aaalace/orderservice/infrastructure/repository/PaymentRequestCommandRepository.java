package com.aaalace.orderservice.infrastructure.repository;

import com.aaalace.orderservice.domain.model.CommandState;
import com.aaalace.orderservice.domain.model.PaymentRequestCommand;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PaymentRequestCommandRepository extends JpaRepository<PaymentRequestCommand, String> {

    List<PaymentRequestCommand> findTop50ByStateOrderByIdAsc(CommandState state);
}
