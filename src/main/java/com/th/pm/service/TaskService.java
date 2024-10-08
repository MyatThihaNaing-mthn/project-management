package com.th.pm.service;

import com.th.pm.dto.TaskDto;
import com.th.pm.dto.TaskRequest;

public interface TaskService {
    TaskDto createTask(TaskRequest request, String userId, String projectId);
    TaskDto updateTask(TaskRequest request, String taskId, String userId, String projectId);
    TaskDto assignUserToTask(String taskId, String projectId, String assignerId, String assigneeId);
    TaskDto removeUserFromTask(String taskId, String projectId, String taskCreatorId, String memberId);
    void removeTask(String taskId, String projectId, String userId);
}
