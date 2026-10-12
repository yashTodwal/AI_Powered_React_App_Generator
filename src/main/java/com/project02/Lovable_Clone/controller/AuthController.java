package com.project02.Lovable_Clone.controller;

import com.project02.Lovable_Clone.dto.auth.AuthResponse;
import com.project02.Lovable_Clone.dto.auth.LoginRequest;
import com.project02.Lovable_Clone.dto.auth.SignupRequest;
import com.project02.Lovable_Clone.dto.auth.UserProfileResponse;
import com.project02.Lovable_Clone.service.AuthService;
import com.project02.Lovable_Clone.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AuthController {
    AuthService authService;
    UserService userService;

    @PostMapping("/signup")
    public ResponseEntity<AuthResponse> signup(@RequestBody SignupRequest request){
        return ResponseEntity.ok(authService.signup(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> signup(@RequestBody LoginRequest request){
        return ResponseEntity.ok(authService.login(request));
    }

    @GetMapping("/me")
    public ResponseEntity<UserProfileResponse> getProfile(){
        Long userId = 1L;
        return ResponseEntity.ok(userService.getProfile(userId));
    }
}
