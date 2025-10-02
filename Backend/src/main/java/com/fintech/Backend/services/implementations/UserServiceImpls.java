package com.fintech.Backend.services.implementations;

import com.fintech.Backend.dto.AddUserDto;
import com.fintech.Backend.repository.UserRepository;
import com.fintech.Backend.dto.UserDto;
import com.fintech.Backend.entity.UserEntity;
import com.fintech.Backend.services.UserServices;
import com.fintech.Backend.services.PasswordService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpls implements UserServices {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;
    private final PasswordService passwordService;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    public UserDto getUserById(Long id) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        return modelMapper.map(user, UserDto.class);
    }

    @Override
    public UserDto createUser(AddUserDto addUserDto) {
        // Check if user with email already exists
        if (userRepository.findByEmail(addUserDto.getEmail()).isPresent()) {
            throw new IllegalArgumentException("User with email " + addUserDto.getEmail() + " already exists");
        }
        
        UserEntity userEntity = modelMapper.map(addUserDto, UserEntity.class);
        // Encrypt password before saving
        userEntity.setPassword(passwordService.encodePassword(addUserDto.getPassword()));
        UserEntity savedUser = userRepository.save(userEntity);
        return modelMapper.map(savedUser, UserDto.class);
    }

    @Override
    public UserDto updateUser(Long id, AddUserDto addUserDto) {
        UserEntity existingUser = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        
        // Update fields
        existingUser.setFullName(addUserDto.getFullName());
        existingUser.setEmail(addUserDto.getEmail());
        existingUser.setRole(addUserDto.getRole());
        existingUser.setCardNumber(addUserDto.getCardNumber());
        existingUser.setExpiryDate(addUserDto.getExpiryDate());
        existingUser.setCvv(addUserDto.getCvv());
        existingUser.setAccountNumber(addUserDto.getAccountNumber());
        if (addUserDto.getPassword() != null && !addUserDto.getPassword().trim().isEmpty()) {
            existingUser.setPassword(passwordService.encodePassword(addUserDto.getPassword()));
        }
        if (addUserDto.getBalance() != null) {
            existingUser.setBalance(addUserDto.getBalance());
        }
        
        UserEntity updatedUser = userRepository.save(existingUser);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public void deleteUser(Long id) {
        if (!userRepository.existsById(id)) {
            throw new IllegalArgumentException("User not found with ID: " + id);
        }
        userRepository.deleteById(id);
    }

    @Override
    public UserDto updateBalance(Long id, BigDecimal balance) {
        UserEntity user = userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found with ID: " + id));
        
        user.setBalance(balance);
        UserEntity updatedUser = userRepository.save(user);
        return modelMapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserDto getUserByEmail(String email) {
        UserEntity user = userRepository.findByEmail(email)
                .orElseThrow(() -> new IllegalArgumentException("User not found with email: " + email));
        return modelMapper.map(user, UserDto.class);
    }
}
