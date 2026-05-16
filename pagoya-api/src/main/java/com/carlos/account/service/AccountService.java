package com.carlos.account.service;

import com.carlos.account.model.Account;
import com.carlos.account.model.AccountStatus;
import com.carlos.account.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.UUID;
@Service
@RequiredArgsConstructor
public class AccountService implements IAccountService {
    private final AccountRepository accountRepository;
    @Override
    public Account create(Account account) {
        if (account.getCustomer() == null || account.getCustomer().getId() == null)
        {
            throw new RuntimeException("El cliente es obligatorio");
        }
        // RN-07: Un cliente no puede tener dos cuentas del mismo tipo
        if
        (accountRepository.existsByCustomer_IdAndType(account.getCustomer().getId(),
                account.getType())) {
            throw new RuntimeException("Ya tiene una cuenta de este tipo");
        }
        account.setAccountNumber(UUID.randomUUID().toString().substring(0,
                12).toUpperCase());
        account.setBalance(BigDecimal.ZERO);
        // RN-08: Una cuenta recien creada inicia con estado ACTIVE
        account.setStatus(AccountStatus.ACTIVE);
        return accountRepository.save(account);
    }
    @Override
    public Account findByAccountNumber(String accountNumber) {
        return accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new RuntimeException("Cuenta no encontrada"));
    }
}
