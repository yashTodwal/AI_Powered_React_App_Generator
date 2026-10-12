package com.project02.Lovable_Clone.dto.auth;

public record AuthResponse(
        String token,
        UserProfileResponse userProfileResponse
) {
}
