package com.aaalace.orderservice.application.service;

import com.aaalace.orderservice.domain.dto.OrderRequestDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class OrderService {

    public void newOrder(OrderRequestDTO request) {
        // todo: create order
        // todo: create paymentRequestCommand
    }

    public void processOrder() {

    }

    private void processPaidOrder() {

    }

    private void processUnpaidOrder() {

    }
}
