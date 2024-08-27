package com.th.pm.mapper;

import java.util.stream.Collectors;
import com.th.pm.dto.CommentDto;
import com.th.pm.dto.ProjectDto;
import com.th.pm.dto.TaskDto;
import com.th.pm.dto.UserDto;
import com.th.pm.model.Comment;
import com.th.pm.model.Project;
import com.th.pm.model.Task;
import com.th.pm.model.User;

public class DtoMapper {
    public static UserDto mapToUserDto(User user){
        UserDto userDto = new UserDto();
        userDto.setId(user.getId().toString());
        String username = String.format("%s %s", user.getFirstname(), user.getLastname());
        userDto.setUsername(username);
        userDto.setEmail(user.getEmail());
        userDto.setProjectsBelonged(user.getProjects().stream().map(project-> DtoMapper.mapToProjectDto(project)).collect(Collectors.toSet()));
        userDto.setTasksAssigned(user.getTasks().stream().map(task -> DtoMapper.mapToTaskDto(task)).collect(Collectors.toSet()));
        return userDto;
    }

    public static TaskDto mapToTaskDto(Task task){
        TaskDto taskDto = new TaskDto();

        return taskDto;
    }

    public static ProjectDto mapToProjectDto(Project project){
        ProjectDto projectDto = new ProjectDto();
        
        return projectDto;
    }

    public static CommentDto mapTCommentDto(Comment comment){
        CommentDto commentDto = new CommentDto();

        return commentDto;
    }
}
