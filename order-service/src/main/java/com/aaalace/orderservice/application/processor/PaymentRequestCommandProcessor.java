package com.aaalace.orderservice.application.processor;

import jakarta.transaction.Transactional;

public class PaymentRequestCommandProcessor {

    @Transactional
    protected void process() {
        // todo: call PaymentRequestProducer
    }
}
