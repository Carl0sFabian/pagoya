package com.carlos.customer.service;

import com.carlos.customer.model.Customer;
import com.carlos.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {
    private final CustomerRepository customerRepository;
    @Override
    public Customer create(Customer customer) {
        // RN-03: El DNI debe ser unico en el sistema
        if (customerRepository.existsByDni(customer.getDni())) {
            throw new RuntimeException("El DNI ya esta registrado");
        }
        // RN-04: Un usuario solo puede tener un perfil de cliente
        if (customer.getUser() == null || customer.getUser().getId() == null) {
            throw new RuntimeException("El usuario es obligatorio");
        }
        if (customerRepository.existsByUser_Id(customer.getUser().getId())) {
            throw new RuntimeException("El usuario ya tiene un perfil de cliente");
        }
        return customerRepository.save(customer);
    }
    @Override
    public Customer findById(Long id) {
        return customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado"));
    }
}