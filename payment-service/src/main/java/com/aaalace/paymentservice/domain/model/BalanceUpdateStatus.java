package com.aaalace.paymentservice.domain.model;

public enum BalanceUpdateStatus {
    FAILED,
    PENDING,
    SUCCESS,

    INSUFFICIENT_FUNDS,
    BALANCE_NOT_EXISTS,
}
