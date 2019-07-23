package com.revolut.transaction.dao;

import com.revolut.transaction.dao.impl.TransactionDaoImpl;

public class TransactionFactory {
    
    public static TransactionDaoImpl getTransactionDao(){
        return new TransactionDaoImpl();
    }
    
}
