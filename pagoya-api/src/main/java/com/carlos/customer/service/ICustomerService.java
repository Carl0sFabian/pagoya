package com.carlos.customer.service;

import com.carlos.customer.dto.CreateCustomerRequest;
import com.carlos.customer.dto.CustomerResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ICustomerService {
    CustomerResponse create(CreateCustomerRequest request);

    CustomerResponse findById(Long id);

    Page<CustomerResponse> findAll(Pageable pageable);
}
