package com.project02.Lovable_Clone.mapper;

import com.project02.Lovable_Clone.dto.member.MemberResponse;
import com.project02.Lovable_Clone.entity.ProjectMember;
import com.project02.Lovable_Clone.entity.User;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel ="spring")
public interface ProjectMemberMapper {

    @Mapping(target = "userId", source = "id")
    @Mapping(target = "projectRole", constant = "OWNER")
    MemberResponse toMemberResponse(User owner);

    @Mapping(target = "userId", source = "user.id")
    @Mapping(target = "username", source = "user.username")
    @Mapping(target = "name", source = "user.name")
    MemberResponse toMemberResponseFromMember(ProjectMember projectMember);
}
