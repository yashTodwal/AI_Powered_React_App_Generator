package com.project02.Lovable_Clone.repository;

import com.project02.Lovable_Clone.entity.ProjectMember;
import com.project02.Lovable_Clone.entity.ProjectMemberId;
import com.project02.Lovable_Clone.enums.ProjectRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectMemberRepository extends JpaRepository<ProjectMember, ProjectMemberId> {

    List<ProjectMember> findByIdProjectId(Long projectId);

    @Query("SELECT pm.projectRole from ProjectMember pm WHERE pm.id.projectId = :projectId AND pm.id.userId = :userId")
    Optional<ProjectRole> findRoleByProjectIdAndUserId(@Param("projectId") Long projectId, @Param("userId") Long userID);

    @Query("SELECT COUNT(pm) from ProjectMember pm WHERE pm.id.userId = :userId AND pm.projectRole = 'OWNER'")
    int countProjectOwnedByUser(@Param("userId") Long userId);
}
