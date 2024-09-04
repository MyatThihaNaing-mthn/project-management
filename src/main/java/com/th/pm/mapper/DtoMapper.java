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
        return userDto;
    }

    public static TaskDto mapToTaskDto(Task task){
        TaskDto taskDto = new TaskDto();
        taskDto.setId(task.getId().toString());
        taskDto.setTitle(task.getTitle());
        taskDto.setContent(task.getContent());
        taskDto.setStatus(task.getStatus().toString());
        taskDto.setPriority(task.getStatus().toString());
        taskDto.setCreatedBy(mapToUserDto(task.getCreatedBy()));
        taskDto.setDeadline(task.getDeadline());
        taskDto.setProjectBelonged(mapToProjectDto(task.getProject()));
        taskDto.setAssignees(task.getUsers().stream().map(user -> DtoMapper.mapToUserDto(user)).collect(Collectors.toSet()));

        return taskDto;
    }

    public static ProjectDto mapToProjectDto(Project project){
        ProjectDto projectDto = new ProjectDto();
        projectDto.setId(project.getId().toString());
        projectDto.setTitle(project.getTitle());
        projectDto.setStatus(project.getStatus().toString());
        projectDto.setCreatedBy(mapToUserDto(project.getCreatedBy()));
        projectDto.setMembers(project.getMembers().stream().map(member -> DtoMapper.mapToUserDto(member)).collect(Collectors.toSet()));
        projectDto.setTasks(project.getTasks().stream().map(task -> DtoMapper.mapToTaskDto(task)).collect(Collectors.toSet()));

        return projectDto;
    }

    public static CommentDto mapToCommentDto(Comment comment){
        CommentDto commentDto = new CommentDto();
        commentDto.setId(comment.getId().toString());
        commentDto.setContent(comment.getContent());
        commentDto.setCreatedAt(comment.getCreatedAt());
        commentDto.setPostedBy(mapToUserDto(comment.getPostedBy()));
        
        if(comment.getParentComment() != null){
            commentDto.setParentComment(mapToCommentDto(comment.getParentComment()));
        }

        if (comment.getTask() != null) {
            commentDto.setTaskBelonged(mapToTaskDto(comment.getTask()));
        }

        if (comment.getReplies() != null) {
            commentDto.setReplies(comment.getReplies().stream()
                .map(DtoMapper::mapToCommentDto)
                .collect(Collectors.toList()));
        }

        return commentDto;
    }
}
