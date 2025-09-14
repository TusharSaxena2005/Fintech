package com.fintech.Backend.services.implementations;

import com.fintech.Backend.dto.AddUserDto;
import com.fintech.Backend.repository.UserRepository;
import com.fintech.Backend.dto.UserDto;
import com.fintech.Backend.entity.UserEntity;
import com.fintech.Backend.services.UserServices;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpls implements UserServices {
    private final UserRepository userRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<UserDto> getAllUsers() {
        return userRepository.findAll()
                .stream()
                .map(user -> modelMapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    public UserDto getUserById(Long id) {
        UserEntity users = userRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Student not found with ID: "+id));
        return modelMapper.map(users, UserDto.class);
    }

    @Override
    public UserDto createUser(AddUserDto addUserDto) {
        UserEntity userEntity = modelMapper.map(addUserDto, UserEntity.class);
        UserEntity user = userRepository.save(userEntity);
        return modelMapper.map(user, UserDto.class);
    }
}
