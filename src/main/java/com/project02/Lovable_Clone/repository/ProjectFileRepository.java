package com.project02.Lovable_Clone.repository;

import com.project02.Lovable_Clone.entity.ProjectFile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectFileRepository extends JpaRepository<ProjectFile, Long> {
    Optional<ProjectFile> findByProjectIdAndPath(Long projectId, String cleanPath);

    List<ProjectFile> findByProjectId(Long projectId);
}
