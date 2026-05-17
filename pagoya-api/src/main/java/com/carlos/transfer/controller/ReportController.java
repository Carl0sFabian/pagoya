package com.carlos.transfer.controller;

import com.carlos.account.dto.AccountSummaryReport;
import com.carlos.transfer.dto.*;
import com.carlos.transfer.service.IReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequiredArgsConstructor
public class ReportController {

    private final IReportService reportService;

    // 1. URL EXACTA DE TU CAPTURA: GET /api/transfers/reports/by-currency
    @GetMapping("/api/transfers/reports/by-currency")
    public List<TransferByCurrencyReport> byCurrency() {
        return reportService.reportByCurrency();
    }

    // 2. URL EXACTA DE TU CAPTURA: GET /api/transfers/reports/by-day?from=2026-04-01&to=2026-04-25
    @GetMapping("/api/transfers/reports/by-day")
    public List<TransferByDayReport> byDay(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to) {
        return reportService.reportByDay(from, to);
    }

    // 3. URL de transferencias por estado alineada al formato de la guía: GET /api/transfers/reports/by-status
    @GetMapping("/api/transfers/reports/by-status")
    public List<TransferByStatusReport> byStatus() {
        return reportService.reportByStatus();
    }

    // 4. URL del resumen de cuentas alineada al formato de la guía: GET /api/accounts/reports/summary
    @GetMapping("/api/accounts/reports/summary")
    public List<AccountSummaryReport> summary() {
        return reportService.reportSummary();
    }
}