package com.aaalace.paymentservice.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.UUID;

@Data
@Entity
@Builder
@Table(name = "update_balance_command")
@NoArgsConstructor
@AllArgsConstructor
public class BalanceUpdateCommand {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id")
    private String userId;

    private BigDecimal amount;

    @Builder.Default
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CommandState state = CommandState.PENDING;

    @Enumerated(EnumType.STRING)
    private BalanceUpdateCommandType type;
}
