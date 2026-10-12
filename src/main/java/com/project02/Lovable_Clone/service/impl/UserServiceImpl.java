package com.project02.Lovable_Clone.service.impl;

import com.project02.Lovable_Clone.dto.auth.UserProfileResponse;
import com.project02.Lovable_Clone.error.ResourceNotFoundException;
import com.project02.Lovable_Clone.repository.UserRepository;
import com.project02.Lovable_Clone.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level= AccessLevel.PRIVATE)
@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    UserRepository userRepository;
    @Override
    public UserProfileResponse getProfile(Long userId) {
        return null;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username).orElseThrow(()-> new ResourceNotFoundException("User",username));
    }
}
