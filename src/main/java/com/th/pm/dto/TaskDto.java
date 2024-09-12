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
    private String status;
    private String priority;
    private UserDto createdBy;
    private BoardDto boardBelonged;
    private Set<UserDto> assignees;
}
