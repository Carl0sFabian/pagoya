package com.carlos.transfer.service;

import com.carlos.account.dto.AccountSummaryReport;
import com.carlos.account.repository.AccountRepository;
import com.carlos.transfer.dto.*;
import com.carlos.transfer.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReportService implements IReportService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;

    @Override
    public List<TransferByCurrencyReport> reportByCurrency() {
        return transferRepository.reportByCurrencyRaw().stream()
                .map(row -> new TransferByCurrencyReport(
                        (String) row[0],
                        ((Number) row[2]).longValue(),
                        new BigDecimal(row[1].toString())
                ))
                .toList();
    }

    @Override
    public List<TransferByDayReport> reportByDay(LocalDate from, LocalDate to) {
        return transferRepository.reportByDayRaw(from, to).stream()
                .map(row -> new TransferByDayReport(
                        row[0] instanceof java.sql.Date ? ((java.sql.Date) row[0]).toLocalDate() : (LocalDate) row[0], // date
                        ((Number) row[2]).longValue(),
                        new BigDecimal(row[1].toString())
                ))
                .toList();
    }

    @Override
    public List<TransferByStatusReport> reportByStatus() {
        return transferRepository.reportByStatusRaw().stream()
                .map(row -> new TransferByStatusReport(
                        (String) row[0],

                        ((Number) row[1]).longValue()
                ))
                .toList();
    }

    @Override
    public List<AccountSummaryReport> reportSummary() {
        return accountRepository.reportSummaryRaw().stream()
                .map(row -> new AccountSummaryReport(
                        (String) row[0],
                        (String) row[1],
                        ((Number) row[2]).longValue(),
                        new BigDecimal(row[3].toString())
                ))
                .toList();
    }
}