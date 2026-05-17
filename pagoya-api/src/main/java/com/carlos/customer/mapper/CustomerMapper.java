package com.carlos.customer.mapper;

import com.carlos.customer.dto.CreateCustomerRequest;
import com.carlos.customer.dto.CustomerResponse;
import com.carlos.customer.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CustomerMapper {
    @Mapping(target = "id", ignore = true)
    Customer toEntity(CreateCustomerRequest request);
    CustomerResponse toResponse(Customer customer);
}