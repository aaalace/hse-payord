package com.aaalace.paymentservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "payment_status_command")
@NoArgsConstructor
@AllArgsConstructor
public class PaymentStatusCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "order_id")
    private String orderId;

    @Enumerated(EnumType.STRING)
    private BalanceUpdateStatus paymentStatus = BalanceUpdateStatus.PENDING;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommandState state = CommandState.PENDING;

    // app relations

    @Column(name = "balance_update_command_id")
    private UUID balanceUpdateCommandId;
}
