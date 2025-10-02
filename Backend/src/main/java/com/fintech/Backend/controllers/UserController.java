package com.fintech.Backend.controllers;

import com.fintech.Backend.dto.AddUserDto;
import com.fintech.Backend.dto.UserDto;
import com.fintech.Backend.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/users")
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
        UserDto createdUser = userServices.createUser(addUserDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserDto> updateUser(@PathVariable Long id, @RequestBody AddUserDto addUserDto) {
        UserDto updatedUser = userServices.updateUser(id, addUserDto);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id) {
        userServices.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/balance")
    public ResponseEntity<UserDto> updateBalance(@PathVariable Long id, @RequestParam BigDecimal balance) {
        UserDto updatedUser = userServices.updateBalance(id, balance);
        return ResponseEntity.ok(updatedUser);
    }

    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@PathVariable String email) {
        UserDto user = userServices.getUserByEmail(email);
        return ResponseEntity.ok(user);
    }
}
