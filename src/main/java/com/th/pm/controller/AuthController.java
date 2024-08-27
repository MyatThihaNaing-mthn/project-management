package com.th.pm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.th.pm.dto.UserDto;
import com.th.pm.dto.UserRegister;
import com.th.pm.service.UserService;
import com.th.pm.validator.ObjectValidator;

@RestController
@RequestMapping("/api/v1/auth")
public class AuthController {
    @Autowired
    private UserService userService;
    @Autowired
    private ObjectValidator<UserRegister> userValidator;

    @PostMapping("/user")
    ResponseEntity<UserDto> registerUser(
        @RequestBody UserRegister userRegister
    ){
        userValidator.doValidation(userRegister);
        UserDto user = userService.createUser(userRegister);
        return new ResponseEntity<>(user, HttpStatus.CREATED);
    }
}
