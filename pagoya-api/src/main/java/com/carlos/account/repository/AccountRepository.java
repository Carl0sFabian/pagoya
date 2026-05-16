package com.carlos.account.repository;

import com.carlos.account.model.Account;
import com.carlos.account.model.AccountType;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
public interface AccountRepository extends JpaRepository<Account, Long> {
    boolean existsByCustomer_IdAndType(Long customerId, AccountType type);
    Optional<Account> findByAccountNumber(String accountNumber);
}