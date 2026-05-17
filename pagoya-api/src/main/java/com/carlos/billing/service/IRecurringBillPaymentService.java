package com.carlos.billing.service;

import com.carlos.billing.dto.CreateRecurringPaymentRequest;
import com.carlos.billing.dto.RecurringBillPaymentResponse;
import java.util.List;

public interface IRecurringBillPaymentService {
    RecurringBillPaymentResponse schedule(CreateRecurringPaymentRequest request);
    List<RecurringBillPaymentResponse> findByCustomer(Long customerId);
    RecurringBillPaymentResponse pause(Long id);
    RecurringBillPaymentResponse resume(Long id);
    void cancel(Long id);
}