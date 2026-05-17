package com.carlos.auth.service;

import com.carlos.auth.dto.RegisterResponse;
import com.carlos.auth.dto.RegisterUserRequest;
import com.carlos.auth.dto.UserResponse;

public interface IUserService {
    RegisterResponse register(RegisterUserRequest request);
}