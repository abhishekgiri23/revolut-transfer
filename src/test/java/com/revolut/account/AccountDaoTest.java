package com.revolut.account;

import com.revolut.account.dao.AccountFactory;
import com.revolut.account.dao.impl.AccountDaoImpl;
import com.revolut.account.dto.Account;
import com.revolut.common.utils.DatabaseUtils;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

import static junit.framework.TestCase.assertTrue;

public class AccountDaoTest {
    
    private static final AccountDaoImpl accountDao = AccountFactory.getAccountDao();
    
    @BeforeClass
    public static void setup() {
        DatabaseUtils.populateData();
    }
    
    @Test
    public void testCreateAccount() {
        BigDecimal balance = new BigDecimal(1000).setScale(4, RoundingMode.HALF_EVEN);
        long aid = accountDao.createAccount(AccountTestData.buildCreateAccountData());
        Account afterCreation = accountDao.getAccountById(aid);
        assertTrue(afterCreation.getUsername().equals("goku"));
        assertTrue(afterCreation.getCurrencyCode().equals("GBP"));
        assertTrue(afterCreation.getBalance().equals(balance));
    }
    @Test
    public void testUpdateAccountBalanceSufficientFund()  {
        BigDecimal withdrawBalance = new BigDecimal(1500).setScale(4, RoundingMode.HALF_EVEN);
        BigDecimal balance = new BigDecimal(2000).setScale(4, RoundingMode.HALF_EVEN);
        int rowsUpdated = accountDao.updateAccountBalance(AccountTestData.buildAccountData());
        assertTrue(rowsUpdated == 1);
        assertTrue(accountDao.getAccountById(1).getBalance().equals(balance));
        int rowsUpdatedW = accountDao.updateAccountBalance(AccountTestData.buildWithdrawAccountData());
        assertTrue(rowsUpdatedW == 1);
        assertTrue(accountDao.getAccountById(1).getBalance().equals(withdrawBalance));
        
    }
    
    @Test(expected = Exception.class)
    public void testUpdateAccountBalanceNotEnoughFund()  {
        int rowsUpdatedW = accountDao.updateAccountBalance(AccountTestData.buildWithdrawAccountDataWithInvalidBalance());
       assertTrue(rowsUpdatedW==0);
    }
    
    
    
}
