package com.carlos.auth.mapper;

import com.carlos.auth.dto.RegisterResponse;
import com.carlos.auth.dto.RegisterUserRequest;
import com.carlos.auth.dto.UserResponse;
import com.carlos.auth.model.User;
import com.carlos.customer.model.Customer;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface UserMapper {

    @Mapping(target = "userId",     source = "user.id")
    @Mapping(target = "email",      source = "user.email")
    @Mapping(target = "role",       source = "user.role.name")
    @Mapping(target = "customerId", source = "customer.id")
    @Mapping(target = "fullName",   source = "customer.fullName")
    @Mapping(target = "dni",        source = "customer.dni")
    RegisterResponse toRegisterResponse(User user, Customer customer);
}