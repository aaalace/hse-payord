package com.aaalace.paymentservice.application.mapper;

import com.aaalace.paymentservice.domain.model.Balance;
import com.aaalace.paymentservice.domain.dto.BalanceDTO;

public class BalanceMapper {

    public static BalanceDTO mapToBalanceDTO(Balance balance) {
        return BalanceDTO.builder()
                .userId(balance.getUserId())
                .balance(balance.getBalance())
                .build();
    }
}
