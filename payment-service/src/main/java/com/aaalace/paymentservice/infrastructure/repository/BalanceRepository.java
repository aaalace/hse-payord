package com.aaalace.paymentservice.infrastructure.repository;

import com.aaalace.paymentservice.domain.model.Balance;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.UUID;

public interface BalanceRepository extends JpaRepository<Balance, UUID> {

    Optional<Balance> findByUserId(String userId);

    @Modifying
    @Transactional
    @Query("UPDATE Balance b SET b.balance = b.balance + :amount WHERE b.userId = :userId")
    void addAmountByUserId(String userId, BigDecimal amount);
}
