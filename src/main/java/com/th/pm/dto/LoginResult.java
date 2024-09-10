package com.th.pm.dto;

import lombok.Data;

@Data
public class LoginResult{
    private UserDto user;
    private String accessToken;
    private String refreshToken;
}
