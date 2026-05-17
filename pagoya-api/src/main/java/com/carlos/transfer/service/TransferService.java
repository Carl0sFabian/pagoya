package com.carlos.transfer.service;
import com.carlos.account.model.Account;
import com.carlos.account.model.AccountStatus;
import com.carlos.account.repository.AccountRepository;
import com.carlos.transfer.model.Transfer;
import com.carlos.transfer.model.TransferStatus;
import com.carlos.transfer.repository.TransferRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestTemplate;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
@Service
@RequiredArgsConstructor
public class TransferService implements ITransferService {
    private final TransferRepository transferRepository;
    private final AccountRepository accountRepository;
    private final RestTemplate restTemplate;
    @Override
    @Transactional
    public Transfer transfer(Transfer transfer) {
        if (transfer.getSourceAccount() == null ||
                transfer.getSourceAccount().getAccountNumber() == null) {
            throw new RuntimeException("La cuenta origen es obligatoria");
        }
        if (transfer.getTargetAccount() == null ||
                transfer.getTargetAccount().getAccountNumber() == null) {
            throw new RuntimeException("La cuenta destino es obligatoria");
        }
        String sourceNumber = transfer.getSourceAccount().getAccountNumber();
        String targetNumber = transfer.getTargetAccount().getAccountNumber();
        // RN-10: No se puede transferir a la misma cuenta de origen
        if (sourceNumber.equals(targetNumber)) {
            throw new RuntimeException("La cuenta origen y destino no pueden ser la misma");
        }
        // RN-09: El monto minimo es S/. 1.00
        if (transfer.getAmount().compareTo(BigDecimal.ONE) < 0) {
            throw new RuntimeException("El monto minimo de transferencia es S/. 1.00");
        }
        Account source = accountRepository.findByAccountNumber(sourceNumber)
                .orElseThrow(() -> new RuntimeException("Cuenta origen no encontrada"));
                        Account target = accountRepository.findByAccountNumber(targetNumber)
                        .orElseThrow(() -> new RuntimeException("Cuenta destino no encontrada"));
                                // RN-05: La cuenta origen debe estar activa
        if (source.getStatus() != AccountStatus.ACTIVE) {
            throw new RuntimeException("La cuenta origen no esta operativa");
        }
        // RN-11: Validar saldo antes de consultar tipo de cambio
        if (source.getBalance().compareTo(transfer.getAmount()) < 0) {
            throw new RuntimeException("Saldo insuficiente");
        }
        BigDecimal finalAmount = transfer.getAmount();
        BigDecimal rate = null;
        // RN-12: Calcular monto con tipo de cambio del momento
        if (transfer.getCurrency() != null &&
                !transfer.getCurrency().equals("PEN")) {
            rate = getExchangeRate(transfer.getCurrency());
            finalAmount = transfer.getAmount().multiply(rate);
        }
        source.setBalance(source.getBalance().subtract(transfer.getAmount()));
        target.setBalance(target.getBalance().add(finalAmount));
        accountRepository.save(source);
        accountRepository.save(target);
        // RN-14: Registrar fecha, hora y tipo de cambio
        transfer.setSourceAccount(source);
        transfer.setTargetAccount(target);
        transfer.setExchangeRate(rate);
        transfer.setStatus(TransferStatus.COMPLETED);
        transfer.setCreatedAt(LocalDateTime.now());
        return transferRepository.save(transfer);
    }
    private BigDecimal getExchangeRate(String currency) {
        String url = "https://api.frankfurter.dev/v2/rates?base=PEN&quotes=" +
                currency;
        Map response = restTemplate.getForObject(url, Map.class);
        Map rates = (Map) response.get("rates");
        return new BigDecimal(rates.get(currency).toString());
    }
    @Override
    public List<Transfer> findByAccountNumber(String accountNumber) {
        return transferRepository.findBySourceAccount_AccountNumber(accountNumber);
    }
}