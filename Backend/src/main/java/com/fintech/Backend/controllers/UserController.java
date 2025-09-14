package com.fintech.Backend.controllers;

import com.fintech.Backend.dto.AddUserDto;
import com.fintech.Backend.dto.UserDto;
import com.fintech.Backend.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users/")
public class UserController {
    private final UserServices userServices;

    @GetMapping
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return ResponseEntity.ok(userServices.getAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
        return ResponseEntity.ok(userServices.getUserById(id));
    }

    @PostMapping("/signup")
    public ResponseEntity<UserDto> createUser(@RequestBody AddUserDto addUserDto) {
        return ResponseEntity.ok(userServices.createUser(addUserDto));
    }
}
