package com.aaalace.paymentservice.domain.dto;

import lombok.Data;
import lombok.NonNull;

import java.math.BigDecimal;

@Data
public class PaymentRequestMessage {

    @NonNull
    private String orderId;

    @NonNull
    private String userId;

    private BigDecimal amount;
}
