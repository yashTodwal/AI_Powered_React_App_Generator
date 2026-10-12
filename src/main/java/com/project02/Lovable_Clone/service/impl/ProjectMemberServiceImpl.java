package com.project02.Lovable_Clone.service.impl;

import com.project02.Lovable_Clone.dto.member.InviteMemberRequest;
import com.project02.Lovable_Clone.dto.member.MemberResponse;
import com.project02.Lovable_Clone.dto.member.UpdateMemberRoleRequest;
import com.project02.Lovable_Clone.entity.Project;
import com.project02.Lovable_Clone.entity.ProjectMember;
import com.project02.Lovable_Clone.entity.ProjectMemberId;
import com.project02.Lovable_Clone.entity.User;
import com.project02.Lovable_Clone.mapper.ProjectMemberMapper;
import com.project02.Lovable_Clone.repository.ProjectMemberRepository;
import com.project02.Lovable_Clone.repository.ProjectRepository;
import com.project02.Lovable_Clone.repository.UserRepository;
import com.project02.Lovable_Clone.security.AuthUtil;
import com.project02.Lovable_Clone.service.ProjectMemberService;
import jakarta.transaction.Transactional;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@Transactional
public class ProjectMemberServiceImpl implements ProjectMemberService {

    ProjectMemberRepository projectMemberRepository;
    ProjectRepository projectRepository;
    ProjectMemberMapper projectMemberMapper;
    UserRepository userRepository;
    AuthUtil authUtil;

    @Override
    @PreAuthorize("@security.canViewMembers(#projectId)")
    public List<MemberResponse> getProjectMembers(Long projectId) {
        Long userId = authUtil.getCurrentUserId();
        Project project = getAccessibleProjectById(projectId, userId);
        return projectMemberRepository.findByIdProjectId(projectId)
                .stream()
                .map(projectMemberMapper::toMemberResponseFromMember)
                .toList();
    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public MemberResponse inviteMember(Long projectId, InviteMemberRequest request) {
        Long userId = authUtil.getCurrentUserId();
        Project project = projectRepository.findAccessibleProjectById(projectId, userId).orElseThrow();
        User invitee = userRepository.findByUsername(request.username()).orElseThrow();
        if(invitee.getId().equals(userId)){
            throw new RuntimeException("Cannot invite Yourself!!");
        }
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, invitee.getId());
        if(projectMemberRepository.existsById(projectMemberId)){
            throw new RuntimeException("Cannot invite same user once again!!");
        }
        ProjectMember member = ProjectMember.builder()
                .Id(projectMemberId)
                .project(project)
                .user(invitee)
                .projectRole(request.role())
                .invitedAt(Instant.now())
                .build();

        projectMemberRepository.save(member);
        return projectMemberMapper.toMemberResponseFromMember(member);
    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public MemberResponse updateMemberRole(Long projectId, Long memberId, UpdateMemberRoleRequest request) {
        Long userId = authUtil.getCurrentUserId();
        Project project = projectRepository.findAccessibleProjectById(projectId, userId).orElseThrow();
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        ProjectMember projectMember = projectMemberRepository.findById(projectMemberId).orElseThrow();
        projectMember.setProjectRole(request.role());
        projectMemberRepository.save(projectMember);
        return projectMemberMapper.toMemberResponseFromMember(projectMember);
    }

    @Override
    @PreAuthorize("@security.canManageMembers(#projectId)")
    public void removeProjectMember(Long projectId, Long memberId) {
        Long userId = authUtil.getCurrentUserId();
        Project project = projectRepository.findAccessibleProjectById(projectId, userId).orElseThrow();
        ProjectMemberId projectMemberId = new ProjectMemberId(projectId, memberId);
        if(!projectMemberRepository.existsById(projectMemberId)){
            throw new RuntimeException("Member not found in Project!!");
        }
        projectMemberRepository.deleteById(projectMemberId);
    }

    //INTERNAL FUNCTIONS

    public Project getAccessibleProjectById(Long projectId, Long userId) {
        return projectRepository.findAccessibleProjectById(projectId, userId).orElseThrow();
    }
}
