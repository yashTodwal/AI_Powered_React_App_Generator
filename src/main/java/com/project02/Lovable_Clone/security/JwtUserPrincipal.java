package com.project02.Lovable_Clone.security;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.GrantedAuthoritiesContainer;

import java.util.List;

public record JwtUserPrincipal(
        Long userId,
        String userName,
        List<GrantedAuthority> authorities
) {
}
