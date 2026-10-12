package com.project02.Lovable_Clone.mapper;

import com.project02.Lovable_Clone.dto.auth.SignupRequest;
import com.project02.Lovable_Clone.dto.auth.UserProfileResponse;
import com.project02.Lovable_Clone.entity.User;
import org.mapstruct.Mapper;

@Mapper(componentModel ="spring")
public interface UserMapper {
    User toEntity(SignupRequest request);
    UserProfileResponse toUserProfileResponse(User user);
}
