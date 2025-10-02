package com.fintech.Backend.services.implementations;

import com.fintech.Backend.dto.LoginDto;
import com.fintech.Backend.dto.TransactionDto;
import com.fintech.Backend.dto.TransactionRequestDto;
import com.fintech.Backend.dto.UserDto;
import com.fintech.Backend.entity.TransactionEntity;
import com.fintech.Backend.entity.UserEntity;
import com.fintech.Backend.repository.TransactionRepository;
import com.fintech.Backend.repository.UserRepository;
import com.fintech.Backend.services.TransactionServices;
import com.fintech.Backend.services.PasswordService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionServices {
    
    private final TransactionRepository transactionRepository;
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordService passwordService;

    @Override
    public UserDto authenticate(LoginDto loginDto) {
        UserEntity user = userRepository.findByEmail(loginDto.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + loginDto.getEmail()));
        
        // Check encrypted password
        if (!passwordService.matches(loginDto.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid password");
        }
        
        // Check role if specified
        if (loginDto.getRole() != null && !user.getRole().equals(loginDto.getRole())) {
            throw new IllegalArgumentException("Invalid role for this user");
        }
        
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    @Transactional
    public TransactionDto addMoney(Long userId, TransactionRequestDto transactionRequest) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        if (transactionRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        
        // Update user balance
        BigDecimal newBalance = user.getBalance().add(transactionRequest.getAmount());
        user.setBalance(newBalance);
        
        // Create transaction record
        TransactionEntity transaction = new TransactionEntity();
        transaction.setUser(user);
        transaction.setType(com.fintech.Backend.entity.TransactionType.DEPOSIT);
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription() != null ? 
                transactionRequest.getDescription() : "Money added to account");
        transaction.setBalanceAfter(newBalance);
        
        // Save both user and transaction
        userRepository.save(user);
        TransactionEntity savedTransaction = transactionRepository.save(transaction);
        
        return modelMapper.map(savedTransaction, TransactionDto.class);
    }

    @Override
    @Transactional
    public TransactionDto withdrawMoney(Long userId, TransactionRequestDto transactionRequest) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        if (transactionRequest.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        
        if (user.getBalance().compareTo(transactionRequest.getAmount()) < 0) {
            throw new IllegalArgumentException("Insufficient balance. Current balance: $" + user.getBalance());
        }
        
        // Update user balance
        BigDecimal newBalance = user.getBalance().subtract(transactionRequest.getAmount());
        user.setBalance(newBalance);
        
        // Create transaction record
        TransactionEntity transaction = new TransactionEntity();
        transaction.setUser(user);
        transaction.setType(com.fintech.Backend.entity.TransactionType.WITHDRAWAL);
        transaction.setAmount(transactionRequest.getAmount());
        transaction.setDescription(transactionRequest.getDescription() != null ? 
                transactionRequest.getDescription() : "Money withdrawn from account");
        transaction.setBalanceAfter(newBalance);
        
        // Save both user and transaction
        userRepository.save(user);
        TransactionEntity savedTransaction = transactionRepository.save(transaction);
        
        return modelMapper.map(savedTransaction, TransactionDto.class);
    }

    @Override
    public List<TransactionDto> getUserTransactions(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        List<TransactionEntity> transactions = transactionRepository.findByUserOrderByTransactionDateDesc(user);
        return transactions.stream()
                .map(transaction -> modelMapper.map(transaction, TransactionDto.class))
                .toList();
    }

    @Override
    public UserDto getUserAccount(Long userId) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + userId));
        
        return modelMapper.map(user, UserDto.class);
    }
}