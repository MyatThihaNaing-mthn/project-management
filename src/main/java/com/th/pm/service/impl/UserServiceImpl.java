package com.th.pm.service.impl;

import java.time.Instant;
import java.util.HashSet;
import java.util.Optional;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import com.th.pm.dto.UserDto;
import com.th.pm.dto.UserRegister;
import com.th.pm.exceptions.EntityNotFoundException;
import com.th.pm.mapper.DtoMapper;
import com.th.pm.model.Board;
import com.th.pm.model.Task;
import com.th.pm.model.User;
import com.th.pm.repository.UserRepository;
import com.th.pm.service.UserService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public UserDto createUser(UserRegister userRegister) {
        User user = new User();
        user.setFirstname(userRegister.getFirstname());
        user.setLastname(userRegister.getLastname());
        user.setCreatedAt(Instant.now());
        user.setEmail(userRegister.getEmail());
        String encodedPassword = passwordEncoder.encode(userRegister.getPassword());
        user.setPassword(encodedPassword);
        user.setProfileImageUrl("fakeurl");
        user.setBoards(new HashSet<Board>());
        user.setAssignedTask(new HashSet<Task>());

        User savedUser = userRepository.save(user);

        return DtoMapper.mapToUserDto(savedUser);
    }

    @Override
    public UserDto findUserByEmail(String email) {
        Optional<User> user = userRepository.findByEmail(email);
        if(!user.isPresent()){
            throw new EntityNotFoundException("User not found with email "+email);
        }
        return DtoMapper.mapToUserDto(user.get());
    }

    @Override
    public User findUserByEmailForAuth(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        return user;
    }

    @Override
    public UserDto findUserById(String id){
        User user = userRepository.findById(UUID.fromString(id)).orElseThrow();
        return DtoMapper.mapToUserDto(user);
    }

}
