package com.carlos.billing.service;

import com.carlos.billing.dto.ServiceProviderResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IServiceProviderService {
    Page<ServiceProviderResponse> findAllActive(Pageable pageable);
}