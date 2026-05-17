package com.carlos.billing.service;

import com.carlos.billing.dto.BillPaymentResponse;
import com.carlos.billing.dto.CreateBillPaymentRequest;
import com.carlos.billing.dto.PaymentByCategoryResponse;
import com.carlos.billing.exception.DuplicateBillPaymentException;
import com.carlos.billing.exception.InactiveProviderException;
import com.carlos.billing.mapper.BillPaymentMapper;
import com.carlos.billing.model.BillPayment;
import com.carlos.billing.model.PaymentStatus;
import com.carlos.billing.model.ServiceProvider;
import com.carlos.billing.repository.BillPaymentRepository;
import com.carlos.billing.repository.ServiceProviderRepository;
import com.carlos.customer.model.Customer;
import com.carlos.customer.repository.CustomerRepository;
import com.carlos.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class BillPaymentService implements IBillPaymentService {

    private final BillPaymentRepository billPaymentRepository;
    private final ServiceProviderRepository providerRepository;
    private final CustomerRepository customerRepository;
    private final BillPaymentMapper billPaymentMapper;

    @Override
    @Transactional
    public BillPaymentResponse pay(CreateBillPaymentRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("cliente no encontrado"));

        ServiceProvider provider = providerRepository.findById(request.providerId())
                .orElseThrow(() -> new ResourceNotFoundException("proveedor no encontrado"));

        if (!provider.isActive()) {
            throw new InactiveProviderException();
        }

        if (billPaymentRepository.existsByCustomer_IdAndProvider_IdAndBillCode(
                customer.getId(), provider.getId(), request.billCode())) {
            throw new DuplicateBillPaymentException();
        }

        BillPayment entity = billPaymentMapper.toEntity(request);
        entity.setCustomer(customer);
        entity.setProvider(provider);
        entity.setStatus(PaymentStatus.PAID);
        entity.setPaidAt(LocalDateTime.now());
        entity.setCreatedAt(LocalDateTime.now());

        return billPaymentMapper.toResponse(billPaymentRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public Page<BillPaymentResponse> findByCustomer(Long customerId, Pageable pageable) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("cliente no encontrado");
        }
        return billPaymentRepository.findByCustomerId(customerId, pageable)
                .map(billPaymentMapper::toResponse);
    }

    @Override
    @Transactional(readOnly = true)
    public List<PaymentByCategoryResponse> reportByCategory(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("cliente no encontrado");
        }
        return billPaymentRepository.getPaymentsByCategory(customerId).stream()
                .map(r -> new PaymentByCategoryResponse(
                        (String) r[0],
                        ((Number) r[1]).longValue(),
                        (BigDecimal) r[2]))
                .toList();
    }
}