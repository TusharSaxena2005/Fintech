package com.fintech.Backend.services;

import com.fintech.Backend.dto.LoginDto;
import com.fintech.Backend.dto.TransactionDto;
import com.fintech.Backend.dto.TransactionRequestDto;
import com.fintech.Backend.dto.UserDto;

import java.util.List;

public interface TransactionServices {
    UserDto authenticate(LoginDto loginDto);
    TransactionDto addMoney(Long userId, TransactionRequestDto transactionRequest);
    TransactionDto withdrawMoney(Long userId, TransactionRequestDto transactionRequest);
    List<TransactionDto> getUserTransactions(Long userId);
    UserDto getUserAccount(Long userId);
}