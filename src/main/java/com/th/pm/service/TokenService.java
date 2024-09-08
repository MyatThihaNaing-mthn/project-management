package com.th.pm.service;

import com.th.pm.dto.UserDto;

public interface TokenService {
    UserDto getUserByRefreshToken(String refreshToken);
}
