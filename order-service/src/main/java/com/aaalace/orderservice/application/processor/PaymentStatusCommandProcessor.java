package com.aaalace.orderservice.application.processor;

import jakarta.transaction.Transactional;

public class PaymentStatusCommandProcessor {

    @Transactional
    protected void process() {
        // todo: call OrderService.processOrderAfterPayment
        // todo: call OrderSocketProducer
    }
}
