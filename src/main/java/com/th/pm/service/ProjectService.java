package com.th.pm.service;

import com.th.pm.dto.ProjectDto;
import com.th.pm.dto.ProjectRequest;
import com.th.pm.dto.ProjectStatus;

public interface ProjectService {
    ProjectDto createProject(ProjectRequest projectRequest, String userId);
    ProjectDto updateProjectStatus(ProjectStatus status);
    ProjectDto findProjectById(String id);
}
