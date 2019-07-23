package com.revolut.transaction;

import com.revolut.account.dao.AccountFactory;
import com.revolut.account.dao.impl.AccountDaoImpl;
import com.revolut.account.dto.Account;
import com.revolut.account.exception.AccountException;
import com.revolut.common.errors.ApplicationException;
import com.revolut.common.utils.DatabaseUtils;
import com.revolut.transaction.dao.TransactionFactory;
import com.revolut.transaction.dao.impl.TransactionDaoImpl;
import com.revolut.transaction.dto.Transaction;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;
import org.junit.After;
import org.junit.BeforeClass;
import org.junit.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static junit.framework.TestCase.assertTrue;

public class TransactionDaoTest {
    
    private static Logger log = Logger.getLogger(TransactionDaoTest.class);
    private static final TransactionDaoImpl transactionDao = TransactionFactory.getTransactionDao();
    private static final AccountDaoImpl accountDao = AccountFactory.getAccountDao();
    
    @BeforeClass
    public static void setup() {
        DatabaseUtils.populateData();
    }
    
    @After
    public void tearDown() {
    
    }
    
    
    @Test
    public void testAccountTransfer() throws ApplicationException {
        
        Transaction transaction = TransactionTestData.buildTransactionData();
        
        long startTime = System.currentTimeMillis();
        
        Account accountFrom = accountDao.getAccountById(3);
        
        Account accountTo = accountDao.getAccountById(2);
        
        transactionDao.transferAccountBalance(transaction);
        long endTime = System.currentTimeMillis();
        
        log.info("TransferAccountBalance finished, time taken: " + (endTime - startTime) + "ms");
        
        assertTrue(accountFrom.getBalance().compareTo(new BigDecimal(3000.0000).setScale(4, RoundingMode.HALF_EVEN)) == 0);
        assertTrue(accountTo.getBalance().equals(new BigDecimal(2000.0000).setScale(4, RoundingMode.HALF_EVEN)));
        
    }
    
    @Test
    public void testTransferFailOnDBLock() throws ApplicationException {
        final String SQL_LOCK_ACC = "SELECT * FROM Account WHERE AccountId = 5 FOR UPDATE";
        Connection conn = null;
        PreparedStatement lockStmt = null;
        ResultSet rs = null;
        Account fromAccount = null;
        
        try {
            conn = DatabaseUtils.getConnection();
            conn.setAutoCommit(false);
            // lock account for writing:
            lockStmt = conn.prepareStatement(SQL_LOCK_ACC);
            rs = lockStmt.executeQuery();
            if (rs.next()) {
                fromAccount.setAccountId(rs.getLong("AccountId"));
                fromAccount.setBalance(rs.getBigDecimal("Balance"));
                fromAccount.setUsername(rs.getString("UserName"));
                fromAccount.setCurrencyCode(rs.getString("CurrencyCode"));
                if (log.isDebugEnabled())
                    log.debug("Locked Account: " + fromAccount);
            }
            
            if (fromAccount == null) {
                throw new AccountException("Locking error during test, SQL = " + SQL_LOCK_ACC);
            }
            
            Transaction transaction = TransactionTestData.buildTransactionData();
            transactionDao.transferAccountBalance(transaction);
            conn.commit();
        } catch (Exception e) {
            log.error("Exception occurred, initiate a rollback");
            try {
                if (conn != null)
                    conn.rollback();
            } catch (SQLException re) {
                log.error("Fail to rollback transaction", re);
            }
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(lockStmt);
        }
        
        assertTrue(accountDao.getAccountById(6).getBalance().equals(new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN)));
        assertTrue(accountDao.getAccountById(5).getBalance().equals(new BigDecimal(5000).setScale(4, RoundingMode.HALF_EVEN)));
        
        
    }
}
