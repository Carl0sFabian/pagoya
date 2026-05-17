package com.carlos.account.service;

import com.carlos.account.dto.AccountBalanceResponse;
import com.carlos.account.dto.AccountResponse;
import com.carlos.account.dto.CreateAccountRequest;
import com.carlos.account.exception.DuplicateAccountTypeException;
import com.carlos.account.mapper.AccountMapper;
import com.carlos.account.model.*;
import com.carlos.account.repository.AccountRepository;
import com.carlos.shared.exception.ResourceNotFoundException;
import com.carlos.account.model.AccountStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {

    private final AccountRepository accountRepository;
    private final AccountMapper accountMapper;

    @Override
    @Transactional
    public AccountResponse create(CreateAccountRequest request) {
        if (accountRepository.existsByCustomerIdAndType(
                request.customerId(), request.type()))
            throw new DuplicateAccountTypeException();
        Account account = Account.builder()
                .accountNumber(UUID.randomUUID().toString().substring(0,12).toUpperCase())
                .balance(BigDecimal.ZERO)
                .status(AccountStatus.ACTIVE)
                .type(request.type())
                .customerId(request.customerId())
                .build();
        return accountMapper.toResponse(accountRepository.save(account));
    }

    @Override
    public AccountBalanceResponse getBalance(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .map(accountMapper::toBalance)
                .orElseThrow(() -> new ResourceNotFoundException(
                        "cuenta " + accountNumber + " no encontrada"));
    }

    @Override
    public Page<AccountResponse> findByCustomer(Long customerId, Pageable pageable) {
        return accountRepository.findByCustomerId(customerId, pageable)
                .map(accountMapper::toResponse);
    }
}