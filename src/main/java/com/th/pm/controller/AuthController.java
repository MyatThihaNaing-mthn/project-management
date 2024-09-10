package com.th.pm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.th.pm.dto.LogInRequest;
import com.th.pm.dto.LoginResult;
import com.th.pm.dto.UserDto;
import com.th.pm.dto.UserRegister;
import com.th.pm.service.AuthService;
import com.th.pm.service.UserService;
import com.th.pm.validator.ObjectValidator;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
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
    ResponseEntity<UserDto> loginUser(
        @RequestBody LogInRequest request,
        HttpServletResponse servletResponse
    ){
        //TODO validation for login
        LoginResult result = authService.performLogin(request);
        
        setRefreshTokenOnResponse( servletResponse, result.getRefreshToken());
        setAccessTokenOnResponse(servletResponse, result.getAccessToken());
        return new ResponseEntity<>(result.getUser(), HttpStatus.OK);
    }

    @GetMapping("/refresh-token")
    ResponseEntity<UserDto> refreshAccessToken(HttpServletRequest request,
    HttpServletResponse response){
        String refreshToken = getRefreshTokenFromRequest(request);
        LoginResult loginResult = authService.refreshAccessToken(refreshToken);

        setRefreshTokenOnResponse(response, loginResult.getRefreshToken());
        setAccessTokenOnResponse(response, loginResult.getAccessToken());
        
        return new ResponseEntity<>(loginResult.getUser(), HttpStatus.OK);
    }

    private String getRefreshTokenFromRequest(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        String refreshToken = null;
        if(cookies != null){
            for(Cookie cookie: cookies){
                if (cookie.getName().equals("refreshToken")) {
                    refreshToken = cookie.getValue();
                    break;
                }
            }
        }
        log.info(refreshToken);
        return refreshToken;
    }

    private void setRefreshTokenOnResponse(HttpServletResponse response, String refreshToken){
        Cookie cookie = new Cookie("refreshToken", refreshToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        response.addCookie(cookie);
    }

    private void setAccessTokenOnResponse(HttpServletResponse response, String accessToken){
        Cookie cookie = new Cookie("accessToken", accessToken);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        response.addCookie(cookie); 
    }
        
}
