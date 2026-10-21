package com.project02.Lovable_Clone.dto.project;

import com.project02.Lovable_Clone.enums.ProjectRole;

import java.time.Instant;

public record ProjectSummaryResponse(
        Long id,
        String name,
        Instant createdAt,
        Instant updatedAt,
        ProjectRole role
) {
}
