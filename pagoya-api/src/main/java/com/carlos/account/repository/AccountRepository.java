package com.carlos.account.repository;

import com.carlos.account.model.Account;
import com.carlos.account.model.AccountType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByCustomerIdAndType(Long customerId, AccountType type);

    Optional<Account> findByAccountNumber(String accountNumber);

    @Query(value = "SELECT * FROM fn_account_summary_report()", nativeQuery = true)
    List<Object[]> reportSummaryRaw();

    Page<Account> findByCustomerId(Long customerId, Pageable pageable);
}