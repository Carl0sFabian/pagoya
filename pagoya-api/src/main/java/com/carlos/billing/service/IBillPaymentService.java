package com.carlos.billing.service;

import com.carlos.billing.dto.BillPaymentResponse;
import com.carlos.billing.dto.CreateBillPaymentRequest;
import com.carlos.billing.dto.PaymentByCategoryResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface IBillPaymentService {
    BillPaymentResponse pay(CreateBillPaymentRequest request);
    Page<BillPaymentResponse> findByCustomer(Long customerId, Pageable pageable);
    List<PaymentByCategoryResponse> reportByCategory(Long customerId);
}