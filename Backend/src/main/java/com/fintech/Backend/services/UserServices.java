package com.fintech.Backend.services;

import com.fintech.Backend.dto.AddUserDto;
import org.springframework.stereotype.Service;
import com.fintech.Backend.dto.UserDto;

import java.util.List;
import java.util.Map;

public interface UserServices {

    public List<UserDto> getAllUsers();
    public UserDto getUserById(Long id);

    public UserDto createUser(AddUserDto addUserDto);
}
