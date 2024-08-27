package com.th.pm.dto;

import lombok.Data;
import java.util.Set;

@Data
public class UserDto {
    private String id;
    private String username;
    private String email;
    private Set<ProjectDto> projectsBelonged;
    private Set<TaskDto> tasksAssigned;
}
