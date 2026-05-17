package com.carlos.auth.service;

import com.carlos.auth.dto.AuthResponse;
import com.carlos.auth.dto.LoginRequest;

public interface IAuthService {
    AuthResponse login(LoginRequest request);
    AuthResponse refresh(String refreshToken);
    void logout(String refreshToken);
    void logoutAll(String email);
}