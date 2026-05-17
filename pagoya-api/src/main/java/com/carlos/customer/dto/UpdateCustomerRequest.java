package com.carlos.customer.dto;

import jakarta.validation.constraints.*;

public record UpdateCustomerRequest(
        @NotBlank(message = "el nombre completo es obligatorio")
        @Size(max = 100)
        String fullName,

        @Pattern(regexp = "\\d{9}", message = "el telefono debe tener 9 digitos")
        String phone
) {}
