package com.revolut.account;

import com.revolut.account.dto.Account;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class AccountTestData {
    
    public static Account buildAccountData() {
        Account account = new Account();
        account.setAccountId(Long.parseLong("1"));
        account.setBalance(BigDecimal.valueOf(1000.00));
        account.setCurrencyCode("GBP");
        account.setUsername("gilly");
        return account;
    }
    
    public static Account buildCreateAccountData() {
        Account account = new Account();
        account.setAccountId(Long.parseLong("1"));
        account.setBalance(BigDecimal.valueOf(1000.00));
        account.setCurrencyCode("GBP");
        account.setUsername("goku");
        return account;
    }
    public static Account buildNewAccountData() {
        Account account = new Account();
        account.setAccountId(Long.parseLong("7"));
        account.setBalance(BigDecimal.valueOf(1000.00));
        account.setCurrencyCode("GBP");
        account.setUsername("ron");
        return account;
    }
    
    
    public static Account buildWithdrawAccountData() {
        Account account = new Account();
        account.setAccountId(Long.parseLong("1"));
        account.setBalance(BigDecimal.valueOf(-500));
        account.setCurrencyCode("GBP");
        account.setUsername("gilly");
        return account;
    }
    
    
    public static Account buildWithdrawAccountDataWithInvalidBalance() {
        Account account = new Account();
        account.setAccountId(Long.parseLong("6"));
        account.setBalance(null);
        account.setCurrencyCode("GBP");
        account.setUsername("john");
        return account;
    }
    
    
}
