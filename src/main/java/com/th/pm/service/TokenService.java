package com.th.pm.service;

import com.th.pm.dto.TokenDto;
import com.th.pm.dto.UserDto;
import com.th.pm.model.Token;

public interface TokenService {
    UserDto getUserByRefreshToken(String refreshToken);
    TokenDto saveToken(Token token);
    void deleteToken(Token token);
}
