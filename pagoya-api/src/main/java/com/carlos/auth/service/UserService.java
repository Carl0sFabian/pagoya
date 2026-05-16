package com.carlos.auth.service;
import com.carlos.auth.model.*;
import com.carlos.auth.repository.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class UserService implements IUserService {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    @Override
    public User register(User user) {
        // RN-01: No puede existir mas de un usuario con el mismo email
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("El email ya esta registrado");
        }
        // RN-02: Todo usuario recibe el rol CUSTOMER al registrarse
        Role role = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new RuntimeException("Rol CUSTOMER no encontrado"));
        user.setVerified(false);
        user.setRole(role);
        return userRepository.save(user);
    }
}