package com.th.pm.dto;

import java.time.LocalDate;
import java.util.Set;

import lombok.Data;

@Data
public class TaskDto {
    private String id;
    private String title;
    private String content;
    private LocalDate deadline;
    private UserDto createdBy;
    private ProjectDto projectBelonged;
    private Set<UserDto> assignees;
}
