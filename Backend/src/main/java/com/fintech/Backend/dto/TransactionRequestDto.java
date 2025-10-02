package com.fintech.Backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class TransactionRequestDto {
    private String type; // DEPOSIT or WITHDRAWAL
    private BigDecimal amount;
    private String description;
}