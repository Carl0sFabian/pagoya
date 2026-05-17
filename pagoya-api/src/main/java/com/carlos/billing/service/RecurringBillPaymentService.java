package com.carlos.billing.service;

import com.carlos.billing.dto.CreateRecurringPaymentRequest;
import com.carlos.billing.dto.RecurringBillPaymentResponse;
import com.carlos.billing.exception.*;
import com.carlos.billing.mapper.RecurringBillPaymentMapper;
import com.carlos.billing.model.*;
import com.carlos.billing.repository.*;
import com.carlos.customer.model.Customer;
import com.carlos.customer.repository.CustomerRepository;
import com.carlos.shared.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.temporal.TemporalAdjusters;
import java.util.List;

@Service
@RequiredArgsConstructor
public class RecurringBillPaymentService implements IRecurringBillPaymentService {

    private final RecurringBillPaymentRepository recurringRepository;
    private final ServiceProviderRepository providerRepository;
    private final CustomerRepository customerRepository;
    private final RecurringBillPaymentMapper recurringMapper;

    @Override
    @Transactional
    public RecurringBillPaymentResponse schedule(CreateRecurringPaymentRequest request) {
        Customer customer = customerRepository.findById(request.customerId())
                .orElseThrow(() -> new ResourceNotFoundException("Cliente no encontrado"));

        ServiceProvider provider = providerRepository.findById(request.providerId())
                .orElseThrow(() -> new ResourceNotFoundException("Proveedor no encontrado"));

        // RN-R01: Solo se puede programar a un proveedor activo.
        if (!provider.isActive()) {
            throw new InactiveProviderException();
        }

        RecurringFrequency frequency = RecurringFrequency.valueOf(request.frequency().toUpperCase());
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime nextRun = calculateNextRun(frequency, request.dayOfMonth(), request.dayOfWeek(), now);

        RecurringBillPayment entity = recurringMapper.toEntity(request);
        entity.setCustomer(customer);
        entity.setProvider(provider);
        entity.setFrequency(frequency);
        entity.setStatus(RecurringStatus.ACTIVE);
        entity.setNextRunAt(nextRun);
        entity.setCreatedAt(now);

        return recurringMapper.toResponse(recurringRepository.save(entity));
    }

    @Override
    @Transactional(readOnly = true)
    public List<RecurringBillPaymentResponse> findByCustomer(Long customerId) {
        if (!customerRepository.existsById(customerId)) {
            throw new ResourceNotFoundException("Cliente no encontrado");
        }
        return recurringRepository.findByCustomerId(customerId).stream()
                .map(recurringMapper::toResponse)
                .toList();
    }

    @Override
    @Transactional
    public RecurringBillPaymentResponse pause(Long id) {
        RecurringBillPayment payment = recurringRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago recurrente no encontrado"));

        if (payment.getStatus() == RecurringStatus.CANCELLED) {
            throw new InvalidStatusTransitionException("No se puede modificar un pago que ya esta cancelado"); // RN-R06
        }
        if (payment.getStatus() != RecurringStatus.ACTIVE) {
            throw new InvalidStatusTransitionException("Solo se puede pausar un pago que este activo"); // RN-R04
        }

        payment.setStatus(RecurringStatus.PAUSED);
        return recurringMapper.toResponse(recurringRepository.save(payment));
    }

    @Override
    @Transactional
    public RecurringBillPaymentResponse resume(Long id) {
        RecurringBillPayment payment = recurringRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago recurrente no encontrado"));

        if (payment.getStatus() == RecurringStatus.CANCELLED) {
            throw new InvalidStatusTransitionException("No se puede modificar un pago que ya esta cancelado"); // RN-R06
        }
        if (payment.getStatus() != RecurringStatus.PAUSED) {
            throw new InvalidStatusTransitionException("Solo se puede reanudar un pago que este pausado"); // RN-R05
        }

        payment.setStatus(RecurringStatus.ACTIVE);
        payment.setNextRunAt(calculateNextRun(payment.getFrequency(), payment.getDayOfMonth(), payment.getDayOfWeek(), LocalDateTime.now()));

        return recurringMapper.toResponse(recurringRepository.save(payment));
    }

    @Override
    @Transactional
    public void cancel(Long id) {
        RecurringBillPayment payment = recurringRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pago recurrente no encontrado"));

        payment.setStatus(RecurringStatus.CANCELLED);
        recurringRepository.save(payment);
    }

    private LocalDateTime calculateNextRun(RecurringFrequency frequency, Integer dayOfMonth, Integer dayOfWeek, LocalDateTime baseTime) {
        if (frequency == RecurringFrequency.MONTHLY) {
            if (dayOfMonth == null || dayOfMonth < 1 || dayOfMonth > 28) {
                throw new InvalidSchedulingException("Si la frecuencia es mensual, se debe indicar el dia del mes (1 al 28)"); // RN-R02
            }
            LocalDateTime target = baseTime.withDayOfMonth(dayOfMonth).withHour(8).withMinute(0).withSecond(0).withNano(0);
            return target.isBefore(baseTime) ? target.plusMonths(1) : target;
        } else {
            if (dayOfWeek == null || dayOfWeek < 1 || dayOfWeek > 7) {
                throw new InvalidSchedulingException("Si la frecuencia es semanal, se debe indicar el dia de la semana (1 al 7)"); // RN-R03
            }
            LocalDateTime target = baseTime.with(TemporalAdjusters.nextOrSame(DayOfWeek.of(dayOfWeek))).withHour(8).withMinute(0).withSecond(0).withNano(0);
            return target.isBefore(baseTime) || target.isEqual(baseTime) ? target.plusWeeks(1) : target;
        }
    }
}