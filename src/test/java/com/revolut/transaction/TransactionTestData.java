package com.revolut.transaction;

import com.revolut.transaction.dto.Transaction;

import java.math.BigDecimal;
import java.math.RoundingMode;

public class TransactionTestData {
    
    public static Transaction buildTransactionData(){
        
        Transaction transaction = new Transaction();
        transaction.setAmount(BigDecimal.valueOf(50).setScale(4, RoundingMode.HALF_EVEN));
        transaction.setCurrencyCode("GBP");
        transaction.setDestinationAccount(Long.parseLong("3"));
        transaction.setSourceAccount(Long.parseLong("4"));
        transaction.setStatus("SUCCESS");
        
        return transaction;
    }
    
    public static Transaction buildTransactionDataWithInvalidAmount(){
        
        Transaction transaction = new Transaction();
        transaction.setAmount(null);
        transaction.setCurrencyCode("GBP");
        transaction.setDestinationAccount(Long.parseLong("3"));
        transaction.setSourceAccount(Long.parseLong("4"));
        transaction.setStatus("SUCCESS");
        
        return transaction;
    }
    
    public static Transaction buildTransactionDataWithInvalidCurrency(){
        
        Transaction transaction = new Transaction();
        transaction.setAmount(null);
        transaction.setCurrencyCode("USD");
        transaction.setDestinationAccount(Long.parseLong("3"));
        transaction.setSourceAccount(Long.parseLong("4"));
        transaction.setStatus("SUCCESS");
        
        return transaction;
    }
}
