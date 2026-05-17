package com.carlos.auth.service;

import com.carlos.auth.dto.RegisterResponse;
import com.carlos.auth.dto.RegisterUserRequest;
import com.carlos.auth.dto.UserResponse;
import com.carlos.auth.exception.EmailAlreadyExistsException;
import com.carlos.auth.mapper.UserMapper;
import com.carlos.auth.model.*;
import com.carlos.auth.repository.*;
import com.carlos.customer.exception.DniAlreadyExistsException;
import com.carlos.customer.model.Customer;
import com.carlos.customer.repository.CustomerRepository;
import com.carlos.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final CustomerRepository customerRepository;
    private final PasswordEncoder passwordEncoder;
    private final UserMapper userMapper;

    @Override
    @Transactional
    public RegisterResponse register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.email())) {
            throw new EmailAlreadyExistsException("El email ya se encuentra registrado");
        }

        // 2. CORREGIDO: Mensaje String agregado
        if (customerRepository.existsByDni(request.dni())) {
            throw new DniAlreadyExistsException("El DNI ya se encuentra registrado");
        }

        Role role = roleRepository.findByName("CUSTOMER")
                .orElseThrow(() -> new ResourceNotFoundException(
                        "no se puede completar el registro en este momento"));

        User user = User.builder()
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))
                .verified(false)
                .role(role)
                .build();
        user = userRepository.save(user);

        Customer customer = Customer.builder()
                .fullName(request.fullName())
                .dni(request.dni())
                .phone(request.phone())
                .user(user)
                .build();
        customer = customerRepository.save(customer);

        return userMapper.toRegisterResponse(user, customer);
    }
}