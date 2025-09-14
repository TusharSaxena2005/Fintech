package com.fintech.Backend.dto;

import lombok.Data;

import java.math.BigDecimal;

@Data
public class AddUserDto {
    private String fullName;
    private String password;
    private String email;
    private String role;
    private String CardNumber;
    private String ExpiryDate;
    private String CVV;
    private String AccountNumber;
    private BigDecimal Balance= BigDecimal.ZERO;
}
