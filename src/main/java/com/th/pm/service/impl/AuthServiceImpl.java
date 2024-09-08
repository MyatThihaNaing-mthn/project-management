package com.th.pm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.th.pm.dto.LogInRequest;
import com.th.pm.dto.LoginResponse;
import com.th.pm.mapper.DtoMapper;
import com.th.pm.model.User;
import com.th.pm.repository.TokenRepository;
import com.th.pm.security.JwtService;
import com.th.pm.service.AuthService;
import com.th.pm.service.UserService;

@Service
public class AuthServiceImpl implements AuthService{
    @Autowired
    private JwtService jwtService;
    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private UserService userService;
    @Autowired
    private TokenRepository tokenRepository;


    @Override
    public LoginResponse performLogin(LogInRequest request) {
        setAuthentication(authenticate(request.getEmail(), request.getPassword()));

        LoginResponse response = new LoginResponse();

        User user = userService.findUserByEmailForAuth(request.getEmail());
        String token = jwtService.generateToken(user);
        
        response.setAccessToken(token);
        response.setUser(DtoMapper.mapToUserDto(user));

        return response;
    }

    private Authentication authenticate(String email, String password){
        Authentication authentication = null;
        try{
            authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email, password));
        }catch(BadCredentialsException e){
            throw new BadCredentialsException("Invalid password");
        }catch(AuthenticationException e){
            throw new BadCredentialsException("Invalid email and password");
        }
        return authentication;
    }


    private void setAuthentication(Authentication authentication){
        SecurityContextHolder.getContext().setAuthentication(authentication);
    }

    @Override
    public LoginResponse refreshAccessToken(String refreshToken) {
        User user = tokenRepository.findUserByToken(refreshToken).orElseThrow();
        String accessToken = jwtService.generateRefreshToken(user);

        LoginResponse response = new LoginResponse();
        response.setAccessToken(accessToken);
        response.setUser(DtoMapper.mapToUserDto(user));

        return response;
    }

    
}
