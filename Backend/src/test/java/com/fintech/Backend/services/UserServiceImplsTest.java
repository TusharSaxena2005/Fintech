package com.fintech.Backend.services;

import com.fintech.Backend.dto.AddUserDto;
import com.fintech.Backend.dto.UserDto;
import com.fintech.Backend.entity.UserEntity;
import com.fintech.Backend.repository.UserRepository;
import com.fintech.Backend.services.implementations.UserServiceImpls;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplsTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private ModelMapper modelMapper;

    @Mock
    private PasswordService passwordService;

    @InjectMocks
    private UserServiceImpls userService;

    private AddUserDto addUserDto;
    private UserEntity userEntity;
    private UserDto userDto;

    @BeforeEach
    void setUp() {
        addUserDto = new AddUserDto();
        addUserDto.setFullName("John Doe");
        addUserDto.setEmail("john@example.com");
        addUserDto.setPassword("password123");
        addUserDto.setRole("USER");
        addUserDto.setAccountNumber("1234567890");
        addUserDto.setBalance(BigDecimal.valueOf(1000));

        userEntity = new UserEntity();
        userEntity.setId(1L);
        userEntity.setFullName("John Doe");
        userEntity.setEmail("john@example.com");
        userEntity.setPassword("encodedPassword");
        userEntity.setRole("USER");
        userEntity.setAccountNumber("1234567890");
        userEntity.setBalance(BigDecimal.valueOf(1000));

        userDto = new UserDto();
        userDto.setId(1L);
        userDto.setFullName("John Doe");
        userDto.setEmail("john@example.com");
        userDto.setRole("USER");
        userDto.setAccountNumber("1234567890");
        userDto.setBalance(BigDecimal.valueOf(1000));
    }

    @Test
    void createUser_ShouldReturnUserDto_WhenValidInput() {
        // Arrange
        when(userRepository.findByEmail(addUserDto.getEmail())).thenReturn(Optional.empty());
        when(modelMapper.map(addUserDto, UserEntity.class)).thenReturn(userEntity);
        when(passwordService.encodePassword(addUserDto.getPassword())).thenReturn("encodedPassword");
        when(userRepository.save(any(UserEntity.class))).thenReturn(userEntity);
        when(modelMapper.map(userEntity, UserDto.class)).thenReturn(userDto);

        // Act
        UserDto result = userService.createUser(addUserDto);

        // Assert
        assertNotNull(result);
        assertEquals(userDto.getFullName(), result.getFullName());
        assertEquals(userDto.getEmail(), result.getEmail());
        verify(passwordService).encodePassword(addUserDto.getPassword());
        verify(userRepository).save(any(UserEntity.class));
    }

    @Test
    void createUser_ShouldThrowException_WhenEmailAlreadyExists() {
        // Arrange
        when(userRepository.findByEmail(addUserDto.getEmail())).thenReturn(Optional.of(userEntity));

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.createUser(addUserDto)
        );
        
        assertEquals("User with email john@example.com already exists", exception.getMessage());
        verify(userRepository, never()).save(any(UserEntity.class));
    }

    @Test
    void getUserById_ShouldReturnUserDto_WhenUserExists() {
        // Arrange
        Long userId = 1L;
        when(userRepository.findById(userId)).thenReturn(Optional.of(userEntity));
        when(modelMapper.map(userEntity, UserDto.class)).thenReturn(userDto);

        // Act
        UserDto result = userService.getUserById(userId);

        // Assert
        assertNotNull(result);
        assertEquals(userDto.getId(), result.getId());
        assertEquals(userDto.getFullName(), result.getFullName());
    }

    @Test
    void getUserById_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        Long userId = 999L;
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.getUserById(userId)
        );
        
        assertEquals("User not found with ID: 999", exception.getMessage());
    }

    @Test
    void deleteUser_ShouldDeleteUser_WhenUserExists() {
        // Arrange
        Long userId = 1L;
        when(userRepository.existsById(userId)).thenReturn(true);

        // Act
        userService.deleteUser(userId);

        // Assert
        verify(userRepository).deleteById(userId);
    }

    @Test
    void deleteUser_ShouldThrowException_WhenUserNotFound() {
        // Arrange
        Long userId = 999L;
        when(userRepository.existsById(userId)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> userService.deleteUser(userId)
        );
        
        assertEquals("User not found with ID: 999", exception.getMessage());
        verify(userRepository, never()).deleteById(userId);
    }
}