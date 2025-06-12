package com.aaalace.orderservice.domain.dto;

public enum BalanceUpdateStatus {
    FAILED,
    PENDING,
    SUCCESS,

    INSUFFICIENT_FUNDS,
    BALANCE_NOT_EXISTS,
}
