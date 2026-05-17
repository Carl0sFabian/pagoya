package com.carlos.account.dto;

import java.math.BigDecimal;

public record AccountSummaryReport(
        String type, String status, Long total, BigDecimal totalBalance) {}