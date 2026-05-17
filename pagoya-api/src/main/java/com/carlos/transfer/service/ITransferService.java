package com.carlos.transfer.service;
import com.carlos.transfer.dto.TransferRequest;
import com.carlos.transfer.dto.TransferResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ITransferService {
    TransferResponse transfer(TransferRequest request);
    Page<TransferResponse> findByAccountNumber(String accountNumber, Pageable pageable);

}
