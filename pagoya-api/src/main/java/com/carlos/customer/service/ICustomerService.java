package com.carlos.customer.service;

import com.carlos.customer.model.Customer;

public interface ICustomerService {
    Customer create(Customer customer);
    Customer findById(Long id);
}
