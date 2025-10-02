package com.fintech.Backend.config;

import com.fintech.Backend.entity.UserEntity;
import com.fintech.Backend.repository.UserRepository;
import com.fintech.Backend.services.PasswordService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
@RequiredArgsConstructor
@Slf4j
public class DataInitializer {

    private final UserRepository userRepository;
    private final PasswordService passwordService;

    @EventListener(ApplicationReadyEvent.class)
    public void initializeData() {
        // Create demo admin user if not exists
        if (userRepository.findByEmail("admin@fintech.com").isEmpty()) {
            UserEntity admin = new UserEntity();
            admin.setFullName("System Administrator");
            admin.setEmail("admin@fintech.com");
            admin.setPassword(passwordService.encodePassword("admin123"));
            admin.setRole("ADMIN");
            admin.setAccountNumber("1000000001");
            admin.setBalance(BigDecimal.valueOf(50000));
            admin.setCardNumber("1234567890123456");
            admin.setExpiryDate("12/28");
            admin.setCvv("123");
            
            userRepository.save(admin);
            log.info("Demo admin user created: admin@fintech.com / admin123");
        }

        // Create demo regular user if not exists
        if (userRepository.findByEmail("user@fintech.com").isEmpty()) {
            UserEntity user = new UserEntity();
            user.setFullName("John Doe");
            user.setEmail("user@fintech.com");
            user.setPassword(passwordService.encodePassword("user123"));
            user.setRole("USER");
            user.setAccountNumber("2000000001");
            user.setBalance(BigDecimal.valueOf(5000));
            user.setCardNumber("9876543210987654");
            user.setExpiryDate("06/27");
            user.setCvv("456");
            
            userRepository.save(user);
            log.info("Demo regular user created: user@fintech.com / user123");
        }
        
        log.info("Data initialization completed");
    }
}