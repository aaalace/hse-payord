package com.aaalace.orderservice.domain.dto;

import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class OrderStatusDTO {

    private String orderId;

    private BalanceUpdateStatus status;
}
