package com.th.pm.dto;

import lombok.Data;

@Data
public class TokenDto {
    private String id;
    private String token;
    private UserDto user;
}
