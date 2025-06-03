package com.aaalace.orderservice.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class PaymentRequestMessage {

    private String orderId;

    private String userId;

    private BigDecimal amount;
}
