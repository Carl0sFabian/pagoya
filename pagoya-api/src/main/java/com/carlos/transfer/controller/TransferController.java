package com.carlos.transfer.controller;

import com.carlos.shared.pagination.PageResponse;
import com.carlos.transfer.dto.TransferRequest;
import com.carlos.transfer.dto.TransferResponse;
import com.carlos.transfer.service.ITransferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
@RestController
@RequestMapping("/api/transfers")
@RequiredArgsConstructor
public class TransferController {

    private final ITransferService transferService;

    @PostMapping
    public ResponseEntity<TransferResponse> transfer(
            @Valid @RequestBody TransferRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(transferService.transfer(request));
    }

    @GetMapping("/account/{accountNumber}")
    public ResponseEntity<PageResponse<TransferResponse>> findByAccountNumber(
            @PathVariable String accountNumber,
            @PageableDefault(size = 10, sort = "createdAt") Pageable pageable) {
        return ResponseEntity.ok(PageResponse.from(
                transferService.findByAccountNumber(accountNumber, pageable)));
    }
}