package com.carlos.account.mapper;

import com.carlos.account.dto.AccountBalanceResponse;
import com.carlos.account.dto.AccountResponse;
import com.carlos.account.model.Account;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface AccountMapper {
    AccountResponse toResponse(Account account);
    AccountBalanceResponse toBalance(Account account);
}