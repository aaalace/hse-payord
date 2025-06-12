package com.aaalace.orderservice.domain.dto;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Data
@Builder
public class PaymentRequestMessage {

    private String orderId;

    private String userId;

    private BigDecimal amount;
}
