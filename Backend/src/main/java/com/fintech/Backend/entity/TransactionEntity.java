package com.fintech.Backend.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class TransactionEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
    
    @Enumerated(EnumType.STRING)
    private TransactionType type; // DEPOSIT, WITHDRAWAL
    
    private BigDecimal amount;
    private String description;
    private LocalDateTime transactionDate;
    private BigDecimal balanceAfter;
    
    @PrePersist
    protected void onCreate() {
        transactionDate = LocalDateTime.now();
    }
}

