package com.carlos.customer.service;

import com.carlos.customer.dto.CreateCustomerRequest;
import com.carlos.customer.dto.CustomerResponse;
import com.carlos.customer.dto.UpdateCustomerRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICustomerService {
    CustomerResponse create(CreateCustomerRequest request);

    CustomerResponse findById(Long id);

    Page<CustomerResponse> findAll(Pageable pageable);
    CustomerResponse findByEmail(String email);
    CustomerResponse updateByEmail(String email, UpdateCustomerRequest request);
    void delete(Long id);
}
