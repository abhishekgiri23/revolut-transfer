package com.revolut.account.dao;

import com.revolut.account.dto.Account;
import com.revolut.account.exception.AccountException;

public interface AccountDao {
    
    int updateAccountBalance(Account account) throws AccountException;
    
    long createAccount(Account account) throws AccountException;
    
    Account getAccountById(long accountId) throws AccountException;
    
}
