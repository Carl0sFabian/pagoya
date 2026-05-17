package com.carlos.auth.service;

import com.carlos.auth.dto.RegisterUserRequest;
import com.carlos.auth.dto.UserResponse;

public interface IUserService {
    UserResponse register(RegisterUserRequest request);
}