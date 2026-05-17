package com.carlos.transfer.service;
import com.carlos.transfer.model.Transfer;

import java.util.List;
public interface ITransferService {
    Transfer transfer(Transfer transfer);
    List<Transfer> findByAccountNumber(String accountNumber);
}
