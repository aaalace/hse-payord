package com.aaalace.paymentservice.presentation.controller;

import com.aaalace.paymentservice.domain.dto.DepositRequest;
import com.aaalace.paymentservice.application.service.BalanceService;
import com.aaalace.paymentservice.domain.generic.GenericJsonResponse;
import com.aaalace.paymentservice.domain.dto.BalanceDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/balance")
@RequiredArgsConstructor
public class BalanceController {

    private final BalanceService balanceService;

    @GetMapping("/{userId}")
    public GenericJsonResponse<BalanceDTO> getBalanceByUserId(@PathVariable String userId) {
        BalanceDTO balanceResponse = balanceService.getByUserId(userId);
        return GenericJsonResponse.success(balanceResponse);
    }

    @PostMapping("/open/{userId}")
    public GenericJsonResponse<BalanceDTO> openBalance(@PathVariable String userId) {
        BalanceDTO balanceResponse = balanceService.open(userId);
        return GenericJsonResponse.success(balanceResponse);
    }

    @PostMapping("/deposit")
    public GenericJsonResponse<BalanceDTO> deposit(@RequestBody DepositRequest payload) {
        balanceService.deposit(payload);
        return GenericJsonResponse.success();
    }
}
