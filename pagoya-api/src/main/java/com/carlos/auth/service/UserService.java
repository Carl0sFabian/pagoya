package com.carlos.auth.service;

import com.carlos.auth.dto.RegisterUserRequest;
import com.carlos.auth.dto.UserResponse;
import com.carlos.auth.mapper.UserMapper;
import com.carlos.auth.model.*;
import com.carlos.auth.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public UserResponse register(RegisterUserRequest request) {

        // RN-01: No puede existir más de un usuario con el mismo email
        if (userRepository.existsByEmail(request.email())) {
            throw new RuntimeException("El email ya esta registrado");
        }

        // RN-02: Todo usuario recibe el rol CUSTOMER al registrarse
        Role role = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Rol CUSTOMER no encontrado"));

        User user = User.builder()
                .email(request.email())
                .password(request.password())
                .verified(false)
                .role(role)
                .build();

        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }
}