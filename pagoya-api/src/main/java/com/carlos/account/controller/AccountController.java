package com.carlos.account.controller;

import com.carlos.account.model.Account;
import com.carlos.account.service.IAccountService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;
@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
public class AccountController {
    private final IAccountService accountService;
    @PostMapping
    public ResponseEntity<Account> create(@RequestBody Account account) {
        return ResponseEntity.status(HttpStatus.CREATED).body(
                accountService.create(account));

    }
    @GetMapping("/{accountNumber}")
    public ResponseEntity<Account> findByNumber(@PathVariable String accountNumber)
    {
        return
                ResponseEntity.ok(accountService.findByAccountNumber(accountNumber));
    }
}
