package com.healthverse.auth.service;

import com.healthverse.auth.dto.AuthResponse;
import com.healthverse.auth.dto.LoginRequest;
import com.healthverse.auth.dto.RefreshTokenRequest;
import com.healthverse.auth.dto.RegisterRequest;
import com.healthverse.auth.dto.UserDto;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);

    AuthResponse refreshToken(RefreshTokenRequest request);

    UserDto getCurrentUser(String email);
}
