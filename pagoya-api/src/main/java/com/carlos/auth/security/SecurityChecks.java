package com.carlos.auth.security;

import com.carlos.customer.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component("securityChecks")
@RequiredArgsConstructor
public class SecurityChecks {

    private final CustomerRepository customerRepository;

    /**
     * Devuelve true si el customer con ese id pertenece al usuario autenticado.
     */
    public boolean isCustomerOwner(Long customerId, Object principal) {
        if (!(principal instanceof UserDetails ud)) return false;
        return customerRepository.findById(customerId)
                .map(c -> c.getUser().getEmail().equals(ud.getUsername()))
                .orElse(false);
    }
}