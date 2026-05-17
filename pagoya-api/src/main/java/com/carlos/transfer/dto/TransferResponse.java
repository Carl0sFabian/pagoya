package com.carlos.transfer.dto;

import com.carlos.transfer.model.TransferStatus;
import java.math.BigDecimal;
import java.time.LocalDateTime;

public record TransferResponse(
        Long id,
        String sourceAccountNumber,
        String targetAccountNumber,
        BigDecimal amount,
        String currency,
        BigDecimal exchangeRate,
        TransferStatus status,
        LocalDateTime createdAt
) {}