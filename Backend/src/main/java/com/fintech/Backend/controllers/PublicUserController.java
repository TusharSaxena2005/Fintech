package com.fintech.Backend.controllers;

import com.fintech.Backend.dto.AddUserDto;
import com.fintech.Backend.dto.UserDto;
import com.fintech.Backend.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
import java.util.HashMap;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class PublicUserController {
    
    private final UserServices userServices;

    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody AddUserDto addUserDto) {
        try {
            // Validation
            if (addUserDto.getFullName() == null || addUserDto.getFullName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Full name is required"));
            }
            if (addUserDto.getEmail() == null || addUserDto.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Email is required"));
            }
            if (addUserDto.getPassword() == null || addUserDto.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("message", "Password is required"));
            }
            if (addUserDto.getRole() == null || addUserDto.getRole().trim().isEmpty()) {
                addUserDto.setRole("USER"); // Default role
            }
            
            UserDto createdUser = userServices.createUser(addUserDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
        } catch (IllegalArgumentException e) {
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", e.getMessage());
            return ResponseEntity.badRequest().body(errorResponse);
        } catch (Exception e) {
            // Log the error for debugging
            System.err.println("Signup error: " + e.getMessage());
            Map<String, String> errorResponse = new HashMap<>();
            errorResponse.put("message", "An unexpected error occurred during signup");
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }
}