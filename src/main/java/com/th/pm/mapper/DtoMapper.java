package com.th.pm.mapper;

import java.util.stream.Collectors;
import com.th.pm.dto.CommentDto;
import com.th.pm.dto.BoardDto;
import com.th.pm.dto.TaskDto;
import com.th.pm.dto.TokenDto;
import com.th.pm.dto.UserDto;
import com.th.pm.model.Comment;
import com.th.pm.model.Board;
import com.th.pm.model.Task;
import com.th.pm.model.Token;
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
        taskDto.setBoardBelonged(mapToBoardDto(task.getBoard()));
        taskDto.setAssignees(task.getAssignees().stream().map(user -> DtoMapper.mapToUserDto(user)).collect(Collectors.toSet()));

        return taskDto;
    }

    public static BoardDto mapToBoardDto(Board board){
        BoardDto boardDto = new BoardDto();
        boardDto.setId(board.getId().toString());
        boardDto.setTitle(board.getTitle());
        boardDto.setStatus(board.getStatus().toString());
        boardDto.setCreatedBy(mapToUserDto(board.getCreatedBy()));
        boardDto.setMembers(board.getMembers().stream().map(member -> DtoMapper.mapToUserDto(member)).collect(Collectors.toSet()));
        boardDto.setTasks(board.getTasks().stream().map(task -> DtoMapper.mapToTaskDto(task)).collect(Collectors.toSet()));

        return boardDto;
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

    public static TokenDto mapToTokenDto(Token token){
        TokenDto dto = new TokenDto();
        dto.setId(token.getId().toString());
        dto.setToken(token.getToken());
        dto.setUser(DtoMapper.mapToUserDto(token.getUser()));
        
        return dto;
    }
}
