package com.aaalace.paymentservice.presentation.controller;

import com.aaalace.paymentservice.domain.dto.BalanceDTO;
import com.aaalace.paymentservice.domain.dto.DepositRequest;
import com.aaalace.paymentservice.domain.generic.GenericJsonResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.UUID;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class BalanceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testOpenAndGetBalance() throws Exception {
        String userId = UUID.randomUUID().toString();

        MvcResult openResult = mockMvc.perform(post("/v1/balance/open/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        GenericJsonResponse<BalanceDTO> openResponse = objectMapper.readValue(
                openResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructParametricType(GenericJsonResponse.class, BalanceDTO.class)
        );

        assertNotNull(openResponse.data);
        assertEquals(userId, openResponse.data.getUserId());
        assertEquals(BigDecimal.ZERO, openResponse.data.getBalance());

        MvcResult getResult = mockMvc.perform(get("/v1/balance/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        GenericJsonResponse<BalanceDTO> getResponse = objectMapper.readValue(
                getResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructParametricType(GenericJsonResponse.class, BalanceDTO.class)
        );

        assertNotNull(getResponse.data);
        assertEquals(userId, getResponse.data.getUserId());
        assertEquals(BigDecimal.ZERO, getResponse.data.getBalance());
    }

    @Test
    void testDeposit() throws Exception {
        String userId = UUID.randomUUID().toString();
        BigDecimal depositAmount = new BigDecimal("100.00");

        mockMvc.perform(post("/v1/balance/open/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());

        DepositRequest depositRequest = new DepositRequest();
        depositRequest.setAmount(depositAmount);
        depositRequest.setUserId(userId);
        mockMvc.perform(post("/v1/balance/deposit")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(depositRequest)))
                .andExpect(status().isOk())
                .andReturn();

        TimeUnit.SECONDS.sleep(3);

        MvcResult getResult = mockMvc.perform(get("/v1/balance/{userId}", userId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        GenericJsonResponse<BalanceDTO> getResponse = objectMapper.readValue(
                getResult.getResponse().getContentAsString(),
                objectMapper.getTypeFactory().constructParametricType(GenericJsonResponse.class, BalanceDTO.class)
        );

        assertNotNull(getResponse.data);
        assertEquals(userId, getResponse.data.getUserId());
        assertEquals(depositAmount, getResponse.data.getBalance());
    }

    @Test
    void testGetNonExistentBalance() throws Exception {
        String nonExistentUserId = UUID.randomUUID().toString();

        mockMvc.perform(get("/v1/balance/{userId}", nonExistentUserId)
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
} 