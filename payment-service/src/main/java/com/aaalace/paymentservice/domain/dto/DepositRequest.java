package com.aaalace.paymentservice.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class DepositRequest {

    private String userId;

    private BigDecimal amount;
}
