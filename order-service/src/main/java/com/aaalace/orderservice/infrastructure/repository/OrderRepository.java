package com.aaalace.orderservice.infrastructure.repository;

import com.aaalace.orderservice.domain.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, String> {

    List<Order> findByUserId(String userId);
}
