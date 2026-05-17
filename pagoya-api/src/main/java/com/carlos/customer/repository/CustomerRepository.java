package com.carlos.customer.repository;
import com.carlos.customer.model.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    boolean existsByDni(String dni);
    boolean existsByUserId(Long userId);
    Optional<Customer> findByUserId(Long userId);   // NUEVO
}