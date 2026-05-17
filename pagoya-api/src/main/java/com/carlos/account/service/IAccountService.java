package com.carlos.account.service;

import com.carlos.account.dto.AccountBalanceResponse;
import com.carlos.account.dto.AccountResponse;
import com.carlos.account.dto.CreateAccountRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IAccountService {
    AccountResponse create(CreateAccountRequest request);

    AccountBalanceResponse getBalance(String accountNumber);

    Page<AccountResponse> findByCustomer(Long customerId, Pageable pageable);
}