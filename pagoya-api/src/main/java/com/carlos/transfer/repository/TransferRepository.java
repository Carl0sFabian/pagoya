package com.carlos.transfer.repository;

import com.carlos.transfer.model.Transfer;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
public interface TransferRepository extends JpaRepository<Transfer, Long> {
    List<Transfer> findBySourceAccount_AccountNumber(String accountNumber);
}
