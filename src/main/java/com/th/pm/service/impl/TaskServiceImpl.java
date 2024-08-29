package com.th.pm.service.impl;

import java.time.Instant;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.orm.jpa.JpaSystemException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Service;

import com.th.pm.dto.TaskDto;
import com.th.pm.dto.TaskRequest;
import com.th.pm.exceptions.DatabaseException;
import com.th.pm.mapper.DtoMapper;
import com.th.pm.model.Comment;
import com.th.pm.model.Project;
import com.th.pm.model.Task;
import com.th.pm.model.User;
import com.th.pm.repository.ProjectRepository;
import com.th.pm.repository.TaskRepository;
import com.th.pm.repository.UserRepository;
import com.th.pm.service.TaskService;

import jakarta.persistence.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TaskServiceImpl implements TaskService{
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private TaskRepository taskRepository;

    @Override
    public TaskDto createTask(TaskRequest request, String userId, String projectId) {
        Project project = validateProject(projectId);
        User user = validateUser(userId, project);

        Set<User> users = new HashSet<>();
        users.add(user);

        Task task = new Task();
        task.setTitle(request.getTitle());
        task.setContent(request.getContent());
        task.setCreatedAt(Instant.now());
        task.setDeadline(request.getDeadline());
        task.setProject(project);
        task.setCreatedBy(user);
        task.setComments(new ArrayList<Comment>());
        task.setUsers(users);

        try{
            Task savedTask = taskRepository.save(task);
            return DtoMapper.mapToTaskDto(savedTask);
        }catch(DataIntegrityViolationException e){
            log.error("Data integerity violation excpetion while saving a new task", e);
            throw new DatabaseException("Data integerity violation excpetion while saving a new task");
        }catch(JpaSystemException e){
            log.error("Jpa system excpetion while saving a new task", e);
            throw new DatabaseException("Jpa system excpetion while saving a new task");
        }
    }

    private Project validateProject(String projectId){
        Optional<Project> project = projectRepository.findById(UUID.fromString(projectId));
        if(!project.isPresent()){
            log.error("Project not found");
            throw new EntityNotFoundException("Project not found");
        }
        return project.get();
    }

    private User validateUser(String userId, Project project){
        Optional<User> user = userRepository.findById(UUID.fromString(userId));
        if(!user.isPresent()){
            log.error("User not found");
            throw new EntityNotFoundException("User not found");
        }
        User taskCreator = user.get();
        Set<User> projectMembers = project.getMembers();
        if(projectMembers.contains(taskCreator)){
            return taskCreator;
        }
        
        log.error("Unauthorized access - User is not a member of the project");
        throw new AccessDeniedException("You are not authorized to create task for this prject");
    }

    private Task validateTaskToUpdate(String taskId, User user){
        Optional<Task> task = taskRepository.findById(UUID.fromString(taskId));
        if(!task.isPresent()){
            log.error("Task not found with Id "+taskId);
            throw new EntityNotFoundException("Task not found ");
        }
        Set<User> assignees = task.get().getUsers();
        if (assignees.contains(user)) {
            return task.get();
        }
        
        log.error("Unauthorized operation by userId"+user.getId()+"to update task");
        throw new AccessDeniedException("You are not authorized to peform this operation");
    }

    private Task validateTask(String taskId){
        Optional<Task> task = taskRepository.findById(UUID.fromString(taskId));
        if(!task.isPresent()){
            log.error("Task not found with Id "+taskId);
            throw new EntityNotFoundException("Task not found ");
        }
        return task.get();
    }
    @Override
    public TaskDto updateTask(TaskRequest request,String taskId, String userId, String projectId) {
        Project project = validateProject(projectId);
        User user = validateUser(userId, project);
        Task task = validateTaskToUpdate(taskId, user);

        // TODO to set prioriy and status
        task.setTitle(request.getTitle());
        task.setContent(request.getContent());
        try{
            Task updatedTask = taskRepository.save(task);
            return DtoMapper.mapToTaskDto(updatedTask);
        }catch(DataIntegrityViolationException e){
            log.error("Data integrity violation while updating task", e);
            throw new DatabaseException("Data integrity violation while updating task");
        }catch(JpaSystemException e){
            log.error("Jpa system exception while updating task", e);
            throw new DatabaseException("Jpa system exception while updating task");
        }
    }

    @Override
    public TaskDto assignUserToTask(String taskId, String projectId, String assignerId, String assigneeId) {
        Project project = validateProject(projectId);
        // anyone in the same project can assign to the task
        validateUser(assignerId, project);
        User assignee = validateUser(assigneeId, project);
        Task task = validateTask(taskId);

        Set<User> users = task.getUsers();
        users.add(assignee);
        try{
            Task updatedTask = taskRepository.save(task);
            return DtoMapper.mapToTaskDto(updatedTask);
        }catch(DataIntegrityViolationException e){
            log.error("Data integrity violation while assigning user to task", e);
            throw new DatabaseException("Data integrity violation while assigning user to task");
        }catch(JpaSystemException e){
            log.error("Jpa system exception while assigning user to the task", e);
            throw new DatabaseException("Jpa system exception while assigning user to the task");
        }
    }

    @Override
    public TaskDto removeUserFromTask(String taskId, String projectId, String taskCreatorId, String memberId) {
        Project project = validateProject(projectId);
        User taskCreator = validateUser(taskCreatorId, project);
        User taskMember = validateUser(memberId, project);
        Task task = validateTask(taskId);

        if(!task.getCreatedBy().getId().equals(taskCreator.getId())){
            log.error("Unauthorized operation by "+taskCreatorId+" to remove user from task");
            throw new AccessDeniedException("You are not authorized to perform this operation");
        }
        Set<User> members = task.getUsers();
        if (members.contains(taskMember)) {
            members.remove(taskMember);
            try{
                Task updatedTask = taskRepository.save(task);
                return DtoMapper.mapToTaskDto(updatedTask);
            }catch(DataIntegrityViolationException e){
                log.error("Data integrity violation while removing user from task", e);
                throw new DatabaseException("Data integrity violation while removing user from task");
            }catch(JpaSystemException e){
                log.error("Jpa system exception while removing user from the task", e);
                throw new DatabaseException("Jpa system exception while removing user from the task");
            }
        }

        return DtoMapper.mapToTaskDto(task);
    }

    @Override
    public void removeTask(String taskId, String projectId, String userId) {
        Project project = validateProject(projectId);
        User user = validateUser(userId, project);
        if(!user.equals(project.getCreatedBy())){
            log.error("Unauthorized operation to remove task "+taskId+" by user"+userId);
            throw new AccessDeniedException("You are not authorized to perform this operation");
        }
        Task task = validateTask(taskId);
        try{
            taskRepository.delete(task);
        }catch(DataIntegrityViolationException e){
            log.error("Data integrity violation while deleting task "+ taskId, e);
            throw new DatabaseException("Data integrity violation while removing user from task");
        }catch(JpaSystemException e){
            log.error("Jpa system exception while deleting task "+taskId, e);
            throw new DatabaseException("Jpa system exception while deleting task");
        }
    }
   
}
