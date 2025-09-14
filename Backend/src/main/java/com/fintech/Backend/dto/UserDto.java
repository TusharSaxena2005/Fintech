package com.fintech.Backend.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private Long id;
    private String fullName;
    private String email;
    private String role;
    private String cardNumber;
    private String expiryDate;
    private String cvv;
    private String accountNumber;
    private BigDecimal balance;
}
