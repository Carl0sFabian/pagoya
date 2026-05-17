package com.carlos.billing.controller;

import com.carlos.billing.dto.CreateRecurringPaymentRequest;
import com.carlos.billing.dto.RecurringBillPaymentResponse;
import com.carlos.billing.service.IRecurringBillPaymentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/recurring-bill-payments")
@RequiredArgsConstructor
public class RecurringBillPaymentController {

    private final IRecurringBillPaymentService recurringService;

    @PostMapping
    public ResponseEntity<RecurringBillPaymentResponse> schedule(@Valid @RequestBody CreateRecurringPaymentRequest request) {
        return new ResponseEntity<>(recurringService.schedule(request), HttpStatus.CREATED);
    }

    @GetMapping("/customer/{id}")
    public ResponseEntity<List<RecurringBillPaymentResponse>> listByCustomer(@PathVariable("id") Long id) {
        return ResponseEntity.ok(recurringService.findByCustomer(id));
    }

    @PatchMapping("/{id}/pause")
    public ResponseEntity<RecurringBillPaymentResponse> pause(@PathVariable("id") Long id) {
        return ResponseEntity.ok(recurringService.pause(id));
    }

    @PatchMapping("/{id}/resume")
    public ResponseEntity<RecurringBillPaymentResponse> resume(@PathVariable("id") Long id) {
        return ResponseEntity.ok(recurringService.resume(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> cancel(@PathVariable("id") Long id) {
        recurringService.cancel(id);
        return ResponseEntity.noContent().build();
    }
}