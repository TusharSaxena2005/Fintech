package com.fintech.Backend.controllers;

import com.fintech.Backend.dto.AddUserDto;
import com.fintech.Backend.dto.LoginDto;
import com.fintech.Backend.dto.UserDto;
import com.fintech.Backend.services.TransactionServices;
import com.fintech.Backend.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {
    
    private final TransactionServices transactionServices;
    private final UserServices userServices;

    @PostMapping("/login")
    public ResponseEntity<UserDto> login(@RequestBody LoginDto loginDto) {
        UserDto user = transactionServices.authenticate(loginDto);
        return ResponseEntity.ok(user);
    }
    
    @PostMapping("/signup")
    public ResponseEntity<UserDto> signup(@RequestBody AddUserDto addUserDto) {
        UserDto createdUser = userServices.createUser(addUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }
}