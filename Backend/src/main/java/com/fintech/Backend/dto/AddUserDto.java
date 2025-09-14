package com.fintech.Backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddUserDto {
    private String fullName;
    private String password;
    private String email;
    private String role;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String accountNumber;
    private BigDecimal balance= BigDecimal.ZERO;
}
