package com.fintech.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TransactionDto {
    private Long id;
    private String type; // DEPOSIT or WITHDRAWAL
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionDate;
    private BigDecimal balanceAfter;
}