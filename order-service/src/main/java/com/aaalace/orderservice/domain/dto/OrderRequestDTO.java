package com.aaalace.orderservice.domain.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderRequestDTO {

    private String userId;

    private BigDecimal price;
}
