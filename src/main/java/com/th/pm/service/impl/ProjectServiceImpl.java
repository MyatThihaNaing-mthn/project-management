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
import org.springframework.stereotype.Service;

import com.th.pm.constant.Status;
import com.th.pm.dto.ProjectDto;
import com.th.pm.dto.ProjectRequest;
import com.th.pm.exceptions.DatabaseException;
import com.th.pm.exceptions.EntityNotFoundException;
import com.th.pm.mapper.DtoMapper;
import com.th.pm.model.Project;
import com.th.pm.model.Task;
import com.th.pm.model.User;
import com.th.pm.repository.ProjectRepository;
import com.th.pm.repository.UserRepository;
import com.th.pm.service.ProjectService;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ProjectServiceImpl implements ProjectService {
    @Autowired
    private ProjectRepository projectRepository;
    @Autowired
    private UserRepository userRepository;

    @Override
    public ProjectDto createProject(ProjectRequest projectRequest, String userId) {
        Project project = new Project();

        project.setCreatedAt(Instant.now());
        project.setTitle(projectRequest.getTitle());
        
        Optional<User> user = userRepository.findById(UUID.fromString(userId));
        if(!user.isPresent()){
            throw new EntityNotFoundException("User not found");
        }

        project.setCreatedBy(user.get());
        project.setStatus(Status.CREATED);
        Set<User> members = new HashSet<>();
        members.add(user.get());
        project.setMembers(members);
        project.setTasks(new ArrayList<Task>());
        
        User updateUser = user.get();
        Set<Project> projects = updateUser.getProjects();
        projects.add(project);
        updateUser.setProjects(projects);

        try{
            Project savedProject = projectRepository.save(project);
            userRepository.save(updateUser);
            return DtoMapper.mapToProjectDto(savedProject);
        }catch(DataIntegrityViolationException e){
            log.info("Data Integrity Violation exception while saving project entity", e);
            throw new DatabaseException("Data Integrity Violation exception while saving project entity", e);
        }catch(JpaSystemException e){
            log.info("Jpa system exception while saving project entity", e);
            throw new DatabaseException("Jpa system exception while saving project entity", e);
        }
        
    }


    
}
