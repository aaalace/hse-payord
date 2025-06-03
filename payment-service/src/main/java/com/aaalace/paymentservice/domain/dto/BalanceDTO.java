package com.aaalace.paymentservice.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class BalanceDTO {

    private String userId;

    private BigDecimal balance;
}
