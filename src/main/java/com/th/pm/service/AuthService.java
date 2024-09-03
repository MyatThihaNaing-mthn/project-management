package com.th.pm.service;

import com.th.pm.dto.LogInRequest;
import com.th.pm.dto.LoginResponse;

public interface AuthService {
    LoginResponse performLogin(LogInRequest request);
}
