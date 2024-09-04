package com.th.pm.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.th.pm.dto.ProjectDto;
import com.th.pm.dto.ProjectRequest;
import com.th.pm.security.UserDetailsImpl;
import com.th.pm.service.ProjectService;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("api/v1/project")
@Slf4j
public class ProjectController {

    @Autowired
    private ProjectService projectService;

    @PostMapping("/creation")
    public ResponseEntity<ProjectDto> createNewProject(@RequestBody ProjectRequest request) {
        String userId = getUserId();
        log.info("inside controller");
        ProjectDto project = projectService.createProject(request, userId);
        return new ResponseEntity<>(project, HttpStatus.CREATED);
    }

    private String getUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.getPrincipal() instanceof UserDetailsImpl userDetails) {
            return userDetails.getId();
        }
        return null;
    }

}
