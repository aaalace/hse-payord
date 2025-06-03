package com.aaalace.paymentservice.domain.dto;

import com.aaalace.paymentservice.domain.model.BalanceUpdateStatus;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PaymentStatusMessage {

    private String orderId;

    private String userId;

    private BalanceUpdateStatus status;
}
