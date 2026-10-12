package com.project02.Lovable_Clone.dto.member;

import com.project02.Lovable_Clone.enums.ProjectRole;

import java.time.Instant;

public record MemberResponse(
        Long userId,
        String username,
        String name,
        //String avatarUrl,
        ProjectRole projectRole,
        Instant invitedAt
) {
}
