package com.th.pm.service;

import com.th.pm.dto.CommentDto;
import com.th.pm.dto.CommentRequest;

public interface CommentService {
    CommentDto createComment(CommentRequest request, String userId, String taskId);
    void deleteComment(String commentId, String userId);
}
