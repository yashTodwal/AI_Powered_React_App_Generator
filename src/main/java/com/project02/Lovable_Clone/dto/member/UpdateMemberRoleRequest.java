package com.project02.Lovable_Clone.dto.member;

import com.project02.Lovable_Clone.enums.ProjectRole;
import jakarta.validation.constraints.NotNull;

public record UpdateMemberRoleRequest(
        @NotNull ProjectRole role) {
}
