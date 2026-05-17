package com.carlos.customer.service;

import com.carlos.customer.dto.CreateCustomerRequest;
import com.carlos.customer.dto.CustomerResponse;
import com.carlos.customer.exception.CustomerProfileAlreadyExistsException;
import com.carlos.customer.exception.DniAlreadyExistsException;
import com.carlos.customer.mapper.CustomerMapper;
import com.carlos.customer.model.Customer;
import com.carlos.customer.repository.CustomerRepository;
import com.carlos.shared.exception.ResourceNotFoundException;
import com.carlos.shared.pagination.PageResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CustomerService implements ICustomerService {

    private final CustomerRepository customerRepository;
    private final CustomerMapper customerMapper;

    @Override
    @Transactional
    public CustomerResponse create(CreateCustomerRequest request) {
        if (customerRepository.existsByDni(request.dni()))
            throw new DniAlreadyExistsException(request.dni());
        if (customerRepository.existsByUserId(request.userId()))
            throw new CustomerProfileAlreadyExistsException();
        Customer entity = customerMapper.toEntity(request);
        return customerMapper.toResponse(customerRepository.save(entity));
    }

    @Override
    public CustomerResponse findById(Long id) {
        return customerRepository.findById(id)
                .map(customerMapper::toResponse)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "cliente con id " + id + " no encontrado"));
    }

    @Override
    public Page<CustomerResponse> findAll(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(customerMapper::toResponse);
    }
}