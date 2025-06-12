package com.aaalace.orderservice.infrastructure.repository;

import com.aaalace.orderservice.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, String> {
}
