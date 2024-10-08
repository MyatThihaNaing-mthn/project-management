package com.th.pm.dto;

import lombok.Data;
import java.time.Instant;
import java.util.List;

@Data
public class CommentDto {
    private String id;
    private String content;
    private UserDto postedBy;
    private Instant createdAt;
    private TaskDto taskBelonged;
    private CommentDto parentComment;
    private List<CommentDto> replies;
}
