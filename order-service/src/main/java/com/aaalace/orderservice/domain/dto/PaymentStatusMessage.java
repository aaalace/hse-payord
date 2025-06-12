package com.aaalace.orderservice.domain.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentStatusMessage {

    private String orderId;

    private String userId;

    private BalanceUpdateStatus status;
}
