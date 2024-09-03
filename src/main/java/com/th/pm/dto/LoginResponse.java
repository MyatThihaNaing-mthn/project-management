package com.th.pm.dto;

import lombok.Data;

@Data
public class LoginResponse{
    private UserDto user;
    private String accessToken;
}
