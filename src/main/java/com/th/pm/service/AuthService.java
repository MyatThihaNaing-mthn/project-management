package com.th.pm.service;

import com.th.pm.dto.LogInRequest;
import com.th.pm.dto.LoginResult;

public interface AuthService {
    LoginResult performLogin(LogInRequest request);
    LoginResult refreshAccessToken(String refreshToken);
    String generateRefreshToken(String email);
}
