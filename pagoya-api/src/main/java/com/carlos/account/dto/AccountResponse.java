package com.carlos.account.dto;

import com.carlos.account.model.AccountStatus;
import com.carlos.account.model.AccountType;

import java.math.BigDecimal;

public record AccountResponse(
        Long id,
        String accountNumber,
        BigDecimal balance,
        AccountStatus status,
        AccountType type,
        Long customerId
) {}