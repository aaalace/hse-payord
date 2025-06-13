package com.aaalace.orderservice.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class OrderDTO {

    private String id;

    private BigDecimal amount;

    private BalanceUpdateStatus status;
}
