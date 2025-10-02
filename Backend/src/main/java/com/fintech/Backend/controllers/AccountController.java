package com.fintech.Backend.controllers;

import com.fintech.Backend.dto.TransactionDto;
import com.fintech.Backend.dto.TransactionRequestDto;
import com.fintech.Backend.dto.UserDto;
import com.fintech.Backend.services.TransactionServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/account")
public class AccountController {
    
    private final TransactionServices transactionServices;

    @GetMapping("/{userId}")
    public ResponseEntity<UserDto> getAccount(@PathVariable Long userId) {
        UserDto user = transactionServices.getUserAccount(userId);
        return ResponseEntity.ok(user);
    }

    @PostMapping("/{userId}/deposit")
    public ResponseEntity<TransactionDto> depositMoney(@PathVariable Long userId, 
                                                      @RequestBody TransactionRequestDto transactionRequest) {
        TransactionDto transaction = transactionServices.addMoney(userId, transactionRequest);
        return ResponseEntity.ok(transaction);
    }

    @PostMapping("/{userId}/withdraw")
    public ResponseEntity<TransactionDto> withdrawMoney(@PathVariable Long userId, 
                                                       @RequestBody TransactionRequestDto transactionRequest) {
        TransactionDto transaction = transactionServices.withdrawMoney(userId, transactionRequest);
        return ResponseEntity.ok(transaction);
    }

    @GetMapping("/{userId}/transactions")
    public ResponseEntity<List<TransactionDto>> getTransactions(@PathVariable Long userId) {
        List<TransactionDto> transactions = transactionServices.getUserTransactions(userId);
        return ResponseEntity.ok(transactions);
    }
}