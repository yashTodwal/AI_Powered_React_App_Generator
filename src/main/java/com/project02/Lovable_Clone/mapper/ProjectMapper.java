package com.project02.Lovable_Clone.mapper;

import com.project02.Lovable_Clone.dto.project.ProjectResponse;
import com.project02.Lovable_Clone.dto.project.ProjectSummaryResponse;
import com.project02.Lovable_Clone.entity.Project;
import org.mapstruct.Mapper;

import java.util.List;

@Mapper(componentModel ="spring")
public interface ProjectMapper {
    ProjectResponse toProjectResponse(Project project);

    ProjectSummaryResponse toProjectSummaryResponse(Project project);

    List<ProjectSummaryResponse> toListOfProjectSummaryResponse(List<Project> projects);
}
