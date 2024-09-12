package com.th.pm.dto;

import java.util.Set;

import lombok.Data;

@Data
public class BoardDto {
    private String id;
    private String title;
    private UserDto createdBy;
    private String status;
    private Set<UserDto> members;
    private Set<TaskDto> tasks;
}
