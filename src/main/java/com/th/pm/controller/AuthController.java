package com.th.pm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.th.pm.dto.LogInRequest;
import com.th.pm.dto.LoginResponse;
import com.th.pm.dto.UserDto;
import com.th.pm.dto.UserRegister;
import com.th.pm.service.AuthService;
import com.th.pm.service.UserService;
import com.th.pm.validator.ObjectValidator;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectValidator<UserRegister> userValidator;
    @Autowired
    private AuthService authService;

    @PostMapping("/user")
    ResponseEntity<UserDto> registerUser(
        @RequestBody UserRegister userRegister
    ){
        userValidator.doValidation(userRegister);
        UserDto user = userService.createUser(userRegister);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }

    @PostMapping("/user/login")
    ResponseEntity<LoginResponse> loginUser(
        @RequestBody LogInRequest request
    ){
        //TODO validation for login
        LoginResponse response = authService.performLogin(request);
        return new ResponseEntity<>(response, HttpStatus.OK);
    }
}
