package com.th.pm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.stereotype.Service;

import com.th.pm.dto.TokenDto;
import com.th.pm.dto.UserDto;
import com.th.pm.exceptions.DatabaseException;
import com.th.pm.mapper.DtoMapper;
import com.th.pm.model.Token;
import com.th.pm.repository.TokenRepository;
import com.th.pm.service.TokenService;

import lombok.extern.slf4j.Slf4j;


@Service
@Slf4j
public class TokenServiceImpl implements TokenService{

    @Autowired
    private TokenRepository tokenRepository;

    @Override
    public UserDto getUserByRefreshToken(String refreshToken) {
        Token token = tokenRepository.findByToken(refreshToken).orElseThrow();
        return DtoMapper.mapToUserDto(token.getUser());
    }

    @Override
    public TokenDto saveToken(Token token) {
        try{
            Token savedToken = tokenRepository.save(token);
            return DtoMapper.mapToTokenDto(savedToken);
        }catch(DataIntegrityViolationException e){
            log.error("Data integrity violation exception while saving token", e);
            throw new DataIntegrityViolationException("Data integrity violation exception while saving token");
        }catch(JpaSystemException e){
            log.error("Jpa system exception while saving token", e);
            throw new DatabaseException("Jpa system exception while saving token");
        }
    }

    @Override
    public void deleteToken(Token token) {
        if(token != null){
            try{
                tokenRepository.delete(token);
            }catch(DataIntegrityViolationException e){
                log.error("Error deleting token", e);
                throw new DataIntegrityViolationException("Error deleting token");
            }catch(JpaSystemException e){
                log.error("Error deleting token", e);
                throw new DatabaseException("Error deleting token");
            }
        }
    }
    
}
