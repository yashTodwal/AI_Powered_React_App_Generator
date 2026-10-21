package com.project02.Lovable_Clone.service.impl;

import com.project02.Lovable_Clone.dto.project.ProjectRequest;
import com.project02.Lovable_Clone.dto.project.ProjectResponse;
import com.project02.Lovable_Clone.dto.project.ProjectSummaryResponse;
import com.project02.Lovable_Clone.entity.Project;
import com.project02.Lovable_Clone.entity.ProjectMember;
import com.project02.Lovable_Clone.entity.ProjectMemberId;
import com.project02.Lovable_Clone.entity.User;
import com.project02.Lovable_Clone.enums.ProjectRole;
import com.project02.Lovable_Clone.error.BadRequestException;
import com.project02.Lovable_Clone.error.ResourceNotFoundException;
import com.project02.Lovable_Clone.mapper.ProjectMapper;
import com.project02.Lovable_Clone.repository.ProjectMemberRepository;
import com.project02.Lovable_Clone.repository.ProjectRepository;
import com.project02.Lovable_Clone.repository.UserRepository;
import com.project02.Lovable_Clone.security.AuthUtil;
import com.project02.Lovable_Clone.service.ProjectService;
import com.project02.Lovable_Clone.service.ProjectTemplateService;
import com.project02.Lovable_Clone.service.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class ProjectServiceImpl implements ProjectService {

    ProjectRepository projectRepository;
    UserRepository userRepository;
    ProjectMapper projectMapper;
    ProjectMemberRepository projectMemberRepository;
    AuthUtil authUtil;
    SubscriptionService subscriptionService;
    ProjectTemplateService projectTemplateService;

    @Override
    public List<ProjectSummaryResponse> getUserProjects() {
        Long userId = authUtil.getCurrentUserId();
        var projectsWithRoles = projectRepository.findAllAccessibleByUser(userId);
        return projectsWithRoles.stream()
                .map(p->projectMapper.toProjectSummaryResponse(p.getProject(), p.getRole()))
                .toList();
    }

    @Override
    @PreAuthorize("@security.canViewProject(#projectId)")
    public ProjectSummaryResponse getUserProjectsById(Long projectId) {
        Long userId = authUtil.getCurrentUserId();
        var projectWithRole = projectRepository.findAccessibleProjectByIdWithRole(projectId, userId)
                .orElseThrow(()->new BadRequestException("Project not found!"));
        return projectMapper.toProjectSummaryResponse(projectWithRole.getProject(), projectWithRole.getRole());
    }

    @Override
    public ProjectResponse createProject(ProjectRequest request) {
        if(!subscriptionService.canCreateNewProject()){
            throw new BadRequestException("User cannot create a new project with current plan, upgrade plan now.");
        }
        Long userId = authUtil.getCurrentUserId();
//        User owner = userRepository.findById(userId).orElseThrow(
//                () -> new ResourceNotFoundException("User: ", userId.toString())
//        );

        User owner = userRepository.getReferenceById(userId);
        Project project = Project.builder()
                .name(request.name())
                .isPublic(false)
                .build();
        project = projectRepository.save(project);
        ProjectMemberId projectMemberId = new ProjectMemberId(project.getId(), owner.getId());
        ProjectMember projectMember = ProjectMember.builder()
                .Id(projectMemberId)
                .projectRole(ProjectRole.OWNER)
                .user(owner)
                .acceptedAt(Instant.now())
                .invitedAt(Instant.now())
                .project(project)
                .build();
        projectMemberRepository.save(projectMember);
        projectTemplateService.initializeProjectFromTemplate(project.getId());

//        return new ProjectResponse(
//                project.getId(),
//                project.getName(),
//                project.getCreatedAt(),
//                project.getUpdatedAt(),
//                null // You can map the owner profile later
//        );
        return projectMapper.toProjectResponse(project);
    }

    @Override
    @PreAuthorize("@security.canEditProject(#projectId)")
    public ProjectResponse updateProject(Long projectId, ProjectRequest request) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleProjectById(projectId,userId);

        project.setName(request.name());
        project = projectRepository.save(project);
        return projectMapper.toProjectResponse(project);
    }

    @Override
    @PreAuthorize("@security.canDeleteProject(#projectId)")
    public void softDelete(Long projectId) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleProjectById(projectId,userId);

        project.setDeletedAt(Instant.now());
        projectRepository.save(project);
    }

    //INTERNAL FUNCTIONS

    public Project getAccessibleProjectById(Long projectId, Long userId){
        return projectRepository.findAccessibleProjectById(projectId,userId)
                .orElseThrow(() -> new ResourceNotFoundException("Project",projectId.toString()));
    }
}
