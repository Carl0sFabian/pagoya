package com.carlos.billing.mapper;

import com.carlos.billing.dto.CreateRecurringPaymentRequest;
import com.carlos.billing.dto.RecurringBillPaymentResponse;
import com.carlos.billing.model.RecurringBillPayment;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface RecurringBillPaymentMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "customer", ignore = true)
    @Mapping(target = "provider", ignore = true)
    @Mapping(target = "status", ignore = true)
    @Mapping(target = "nextRunAt", ignore = true)
    @Mapping(target = "createdAt", ignore = true)
    RecurringBillPayment toEntity(CreateRecurringPaymentRequest request);

    @Mapping(source = "customer.id", target = "customerId")
    @Mapping(source = "provider.id", target = "providerId")
    @Mapping(source = "provider.name", target = "providerName")
    RecurringBillPaymentResponse toResponse(RecurringBillPayment entity);
}