package com.th.pm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import com.th.pm.dto.LogInRequest;
import com.th.pm.dto.LoginResult;
import com.th.pm.exceptions.DatabaseException;
import com.th.pm.mapper.DtoMapper;
import com.th.pm.model.Token;
import com.th.pm.model.User;
import com.th.pm.repository.TokenRepository;
import com.th.pm.repository.UserRepository;
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
    @Autowired
    private UserRepository userRepository;


    @Override
    public LoginResult performLogin(LogInRequest request) {
        setAuthentication(authenticate(request.getEmail(), request.getPassword()));

        LoginResult result = new LoginResult();

        User user = userService.findUserByEmailForAuth(request.getEmail());
        String accesstoken = jwtService.generateToken(user);
        String refreshToken = jwtService.generateRefreshToken(user);

        Token token = new Token();
        token.setToken(refreshToken);
        token.setUser(user);

        user.addToken(token);

        userRepository.save(user);
        
        result.setAccessToken(accesstoken);
        result.setRefreshToken(refreshToken);
        result.setUser(DtoMapper.mapToUserDto(user));

        return result;
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
    public LoginResult refreshAccessToken(String refreshToken) {
        Token token = tokenRepository.findByToken(refreshToken).orElseThrow();
        User user = token.getUser();

        String accessToken = jwtService.generateRefreshToken(token.getUser());
        String newRefreshToken = jwtService.generateRefreshToken(token.getUser());

        Token newToken = new Token();
        newToken.setToken(newRefreshToken);
        newToken.setUser(user);

        user.removeToken(token);
        user.addToken(newToken);
        User savedUser = userRepository.save(user);

        LoginResult result = new LoginResult();
        result.setAccessToken(accessToken);
        result.setRefreshToken(newRefreshToken);
        result.setUser(DtoMapper.mapToUserDto(savedUser));

        return result;
    }

    @Override
    public String generateRefreshToken(String email) {
        User user = userRepository.findByEmail(email).orElseThrow();
        String refreshToken = jwtService.generateRefreshToken(user);
        Token token = new Token();
        token.setToken(refreshToken);
        token.setUser(user);
        try{
            tokenRepository.save(token);
            
        }catch(DataIntegrityViolationException e){
            throw new DataIntegrityViolationException("Data integrity violation while saving token");
        }catch(JpaSystemException e){
            throw new DatabaseException("Jpa Exception while saving token");
        }
        return refreshToken;
    }

    
}
