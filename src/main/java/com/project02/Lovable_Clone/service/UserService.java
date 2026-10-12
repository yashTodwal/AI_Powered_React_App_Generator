package com.project02.Lovable_Clone.service;

import com.project02.Lovable_Clone.dto.auth.UserProfileResponse;
import org.jspecify.annotations.Nullable;

public interface UserService {
    UserProfileResponse getProfile(Long userId);
}
