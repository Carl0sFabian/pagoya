package com.carlos.billing.dto;

import jakarta.validation.constraints.*;
import java.math.BigDecimal;

public record CreateRecurringPaymentRequest(
        @NotNull(message = "El ID del cliente es obligatorio") Long customerId,
        @NotNull(message = "El ID del proveedor es obligatorio") Long providerId,
        @NotBlank(message = "El código de recibo es obligatorio") String billCode,
        @NotNull(message = "El monto es obligatorio") @Positive(message = "El monto debe ser mayor a 0") BigDecimal amount,
        @NotBlank(message = "La frecuencia (MONTHLY/WEEKLY) es obligatoria") String frequency,
        Integer dayOfMonth,
        Integer dayOfWeek
) {}