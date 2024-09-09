package com.th.pm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.th.pm.dto.UserDto;
import com.th.pm.mapper.DtoMapper;
import com.th.pm.model.Token;
import com.th.pm.repository.TokenRepository;
import com.th.pm.service.TokenService;


@Service
public class TokenServiceImpl implements TokenService{

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public UserDto getUserByRefreshToken(String refreshToken) {
        Token token = tokenRepository.findByToken(refreshToken).orElseThrow();
        return DtoMapper.mapToUserDto(token.getUser());
    }
    
}
