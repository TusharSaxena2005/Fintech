package com.fintech.Backend.services;

import com.fintech.Backend.dto.AddUserDto;
import com.fintech.Backend.dto.UserDto;

import java.math.BigDecimal;
import java.util.List;

public interface UserServices {

    List<UserDto> getAllUsers();
    UserDto getUserById(Long id);
    UserDto createUser(AddUserDto addUserDto);
    UserDto updateUser(Long id, AddUserDto addUserDto);
    void deleteUser(Long id);
    UserDto updateBalance(Long id, BigDecimal balance);
    UserDto getUserByEmail(String email);
}
