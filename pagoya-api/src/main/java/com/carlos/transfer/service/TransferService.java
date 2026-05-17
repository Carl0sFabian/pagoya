package com.carlos.transfer.service;

import com.carlos.account.exception.AccountNotOperativeException;
import com.carlos.account.model.Account;
import com.carlos.account.model.AccountStatus;
import com.carlos.account.repository.AccountRepository;
import com.carlos.shared.exception.ResourceNotFoundException;
import com.carlos.transfer.dto.TransferRequest;
import com.carlos.transfer.dto.TransferResponse;
import com.carlos.transfer.exception.InsufficientBalanceException;
import com.carlos.transfer.exception.SameAccountTransferException;
import com.carlos.transfer.mapper.TransferMapper;
import com.carlos.transfer.model.Transfer;
import com.carlos.transfer.model.TransferStatus;
import com.carlos.transfer.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class TransferService implements ITransferService {

    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final TransferMapper transferMapper;
    private final RestTemplate restTemplate;

    @Override
    @Transactional
    public TransferResponse transfer(TransferRequest request) {
        if (request.sourceAccountNumber().equals(request.targetAccountNumber()))
            throw new SameAccountTransferException();

        Account source = accountRepository
                .findByAccountNumber(request.sourceAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "cuenta origen no encontrada"));
        Account target = accountRepository
                .findByAccountNumber(request.targetAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException(
                        "cuenta destino no encontrada"));

        if (source.getStatus() != AccountStatus.ACTIVE)
            throw new AccountNotOperativeException();
        if (source.getBalance().compareTo(request.amount()) < 0)
            throw new InsufficientBalanceException();

        BigDecimal finalAmount = request.amount();
        BigDecimal rate = null;
        if (!"PEN".equals(request.currency())) {
            rate = getExchangeRate(request.currency());
            finalAmount = request.amount().multiply(rate);
        }
        source.setBalance(source.getBalance().subtract(request.amount()));
        target.setBalance(target.getBalance().add(finalAmount));
        accountRepository.save(source);
        accountRepository.save(target);

        Transfer transfer = Transfer.builder()
                .sourceAccountNumber(request.sourceAccountNumber())
                .targetAccountNumber(request.targetAccountNumber())
                .amount(request.amount())
                .currency(request.currency())
                .exchangeRate(rate)
                .status(TransferStatus.COMPLETED)
                .createdAt(LocalDateTime.now())
                .build();
        return transferMapper.toResponse(transferRepository.save(transfer));
    }

    @Override
    public Page<TransferResponse> findByAccountNumber(
            String accountNumber, Pageable pageable) {
        return transferRepository
                .findBySourceAccountNumber(accountNumber, pageable)
                .map(transferMapper::toResponse);
    }

    private BigDecimal getExchangeRate(String currency) {
        String url = "https://api.frankfurter.dev/v2/rates?base=PEN&quotes=" + currency;
        Map response = restTemplate.getForObject(url, Map.class);
        Map rates = (Map) response.get("rates");
        return new BigDecimal(rates.get(currency).toString());
    }
}