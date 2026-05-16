package com.carlos.account.service;

import com.carlos.account.model.Account;

public interface IAccountService {
    Account create(Account account);
    Account findByAccountNumber(String accountNumber);
}
