package com.th.pm.service.impl;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Optional;
import java.util.UUID;
import java.time.Instant;
import com.th.pm.dto.CommentDto;
import com.th.pm.dto.CommentRequest;
import com.th.pm.exceptions.DatabaseException;
import com.th.pm.exceptions.EntityNotFoundException;
import com.th.pm.mapper.DtoMapper;
import com.th.pm.model.Comment;
import com.th.pm.model.Task;
import com.th.pm.model.User;
import com.th.pm.repository.CommentRepository;
import com.th.pm.repository.TaskRepository;
import com.th.pm.repository.UserRepository;
import com.th.pm.service.CommentService;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class CommentServiceImpl implements CommentService{

    @Autowired
    private CommentRepository commentRepository;
    @Autowired
    private TaskRepository taskRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public CommentDto createComment(CommentRequest request, String userId, String taskId) {
        Task task = validateTask(taskId);
        User user = validateUserForComment(userId);
        if(!task.getAssignees().contains(user)){
            log.error("Unauthoirzed operation to comment by user"+userId);
            throw new AccessDeniedException("You are not authorized to perform this operation");
        }
        Comment comment = new Comment();
        comment.setContent(request.getContent());
        comment.setCreatedAt(Instant.now());
        comment.setParentComment(null);
        comment.setReplies(new ArrayList<Comment>());
        comment.setPostedBy(user);
        comment.setTask(task);

        try{
            Comment savedComment = commentRepository.save(comment);
            return DtoMapper.mapToCommentDto(savedComment);
        }catch(DataIntegrityViolationException e){
            log.info("Data Integrity Violation exception while saving project entity", e);
            throw new DatabaseException("Data Integrity Violation exception while saving project entity", e);
        }catch(JpaSystemException e){
            log.info("Jpa system exception while saving project entity", e);
            throw new DatabaseException("Jpa system exception while saving project entity", e);
        }
    }

    @Override
    public void deleteComment(String commentId, String userId) {
        validateUserForComment(userId);
        Comment comment = validateComment(commentId);
        try{
            commentRepository.delete(comment);
        }catch(DataIntegrityViolationException e){
            log.info("Data Integrity Violation exception while deleting comment", e);
            throw new DatabaseException("Data Integrity Violation exception while deleting comment", e);
        }catch(JpaSystemException e){
            log.info("Jpa system exception while delting comment", e);
            throw new DatabaseException("Jpa system exception while deleting comment", e);
        }
    }

    @Override
    public CommentDto createReply(CommentRequest request, String userId, String commentId) {
        Comment parentComment = validateComment(commentId);
        User user = validateUserForComment(userId);
        Task task = validateTask(parentComment.getTask().getId().toString());
        if(!task.getAssignees().contains(user)){
            log.error("Access denied for user "+userId+ "to comment on"+ commentId);
            throw new AccessDeniedException("You are not authorized to comment");
        }
        Comment replyComment = new Comment();
        replyComment.setContent(request.getContent());
        replyComment.setCreatedAt(Instant.now());
        replyComment.setParentComment(parentComment);
        replyComment.setPostedBy(user);
        replyComment.setReplies(new ArrayList<Comment>());
        replyComment.setTask(task);

        try{
            Comment reply = commentRepository.save(replyComment);
            return DtoMapper.mapToCommentDto(reply);
        }catch(DataIntegrityViolationException e){
            log.info("Data Integrity Violation exception while creating reply", e);
            throw new DatabaseException("Data Integrity Violation exception while creating reply", e);
        }catch(JpaSystemException e){
            log.info("Jpa system exception while creating reply", e);
            throw new DatabaseException("Jpa system exception while creating reply", e);
        }
    }

    private Task validateTask(String taskId){
        Optional<Task> task = taskRepository.findById(UUID.fromString(taskId));
        if(task.isPresent()){
            return task.get();
        }
        log.error("Task not found with id"+taskId);
        throw new EntityNotFoundException("Task not found with id "+taskId);
    }

    private Comment validateComment(String commentId){
        Optional<Comment> comment = commentRepository.findById(UUID.fromString(commentId));
        if(comment.isPresent()){
            return comment.get();
        }
        throw new EntityNotFoundException("Comment not foun with id "+commentId);
    }

    private User validateUserForComment(String userId){
        Optional<User> user = userRepository.findById(UUID.fromString(userId));
        if(!user.isPresent()){
            log.error("User not found with id"+userId);
            throw new EntityNotFoundException("User not found with id "+userId);
        }
        return user.get();
    }
    
}
