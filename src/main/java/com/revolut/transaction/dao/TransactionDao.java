package com.revolut.transaction.dao;

import com.revolut.account.exception.AccountException;
import com.revolut.transaction.dto.Transaction;

public interface TransactionDao {
    
    int transferAccountBalance(Transaction userTransaction) throws AccountException;
    
}
