package com.th.pm.service.impl;

import org.springframework.stereotype.Service;

import com.th.pm.dto.CommentDto;
import com.th.pm.dto.CommentRequest;
import com.th.pm.service.CommentService;

@Service
public class CommentServiceImpl implements CommentService{

    @Override
    public CommentDto createComment(CommentRequest request, String userId, String taskId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'createComment'");
    }

    @Override
    public void deleteComment(String commentId, String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteComment'");
    }
    
}
