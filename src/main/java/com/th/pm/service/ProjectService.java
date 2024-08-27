package com.th.pm.service;

import com.th.pm.dto.ProjectDto;
import com.th.pm.dto.ProjectRequest;

public interface ProjectService {
    ProjectDto createProject(ProjectRequest projectRequest, String userId);
}
