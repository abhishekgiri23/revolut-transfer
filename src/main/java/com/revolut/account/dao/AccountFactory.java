package com.revolut.account.dao;

import com.revolut.account.dao.impl.AccountDaoImpl;

public class AccountFactory {
    
    public static AccountDaoImpl getAccountDao(){
        return new AccountDaoImpl();
    }
    
}
