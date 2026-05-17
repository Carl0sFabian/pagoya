package com.carlos.transfer.service;

import com.carlos.account.dto.AccountSummaryReport;
import com.carlos.transfer.dto.*;
import java.time.LocalDate;
import java.util.List;

public interface IReportService {
    List<TransferByCurrencyReport> reportByCurrency();
    List<TransferByDayReport> reportByDay(LocalDate from, LocalDate to);
    List<TransferByStatusReport> reportByStatus();
    List<AccountSummaryReport> reportSummary();
}