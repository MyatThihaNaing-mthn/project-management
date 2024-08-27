package com.th.pm.dto;

import java.util.Set;

import lombok.Data;

@Data
public class ProjectDto {
    private String id;
    private String title;
    private String profileImageUrl;
    private UserDto createdBy;
    private Set<UserDto> members;
    private Set<TaskDto> tasks;
}
