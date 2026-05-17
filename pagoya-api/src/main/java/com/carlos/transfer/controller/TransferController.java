package com.carlos.transfer.controller;

import com.carlos.transfer.model.Transfer;
import com.carlos.transfer.service.ITransferService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {
    private final ITransferService transferService;
    @PostMapping
    public ResponseEntity<Transfer> transfer(@RequestBody Transfer transfer) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                transferService.transfer(transfer));
    }
    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<List<Transfer>> findByAccountNumber(@PathVariable String
                                                                      accountNumber) {
        return
                ResponseEntity.ok(transferService.findByAccountNumber(accountNumber));
    }
}