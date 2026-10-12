package com.project02.Lovable_Clone.service;

import com.project02.Lovable_Clone.dto.auth.AuthResponse;
import com.project02.Lovable_Clone.dto.auth.LoginRequest;
import com.project02.Lovable_Clone.dto.auth.SignupRequest;
import org.jspecify.annotations.Nullable;

public interface AuthService {

    AuthResponse signup(SignupRequest request);

    AuthResponse login(LoginRequest request);
}
