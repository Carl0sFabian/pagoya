package com.carlos.account.dto;

import java.math.BigDecimal;

public record AccountBalanceResponse(
        String accountNumber,
        BigDecimal balance
) {}
