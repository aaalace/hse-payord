package com.aaalace.orderservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Entity
@Builder
@Table(name = "payment_request_command")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentRequestCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "order_id")
    private String orderId;

    @Column(name = "user_id")
    private String userId;

    private BigDecimal amount;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommandState state = CommandState.PENDING;
}