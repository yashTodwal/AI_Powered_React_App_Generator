package com.project02.Lovable_Clone.service;

import com.project02.Lovable_Clone.dto.project.ProjectRequest;
import com.project02.Lovable_Clone.dto.project.ProjectResponse;
import com.project02.Lovable_Clone.dto.project.ProjectSummaryResponse;
import org.jspecify.annotations.Nullable;

import java.util.List;

public interface ProjectService {
    List<ProjectSummaryResponse> getUserProjects();

    ProjectSummaryResponse getUserProjectsById(Long id);

    ProjectResponse createProject(ProjectRequest request);

    ProjectResponse updateProject(Long id, ProjectRequest request);

    void softDelete(Long id);
}
