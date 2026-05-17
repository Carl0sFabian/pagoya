package com.carlos.billing.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record RecurringBillPaymentResponse(
        Long id,
        Long customerId,
        Long providerId,
        String providerName,
        String billCode,
        BigDecimal amount,
        String frequency,
        Integer dayOfMonth,
        Integer dayOfWeek,
        String status,
        LocalDateTime nextRunAt,
        LocalDateTime createdAt
) {}