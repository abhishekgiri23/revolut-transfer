package com.revolut.transaction.dao.impl;

import com.revolut.account.dto.Account;
import com.revolut.account.exception.AccountException;
import com.revolut.account.exception.NotSufficientBalanceException;
import com.revolut.common.utils.DatabaseUtils;
import com.revolut.transaction.dao.TransactionDao;
import com.revolut.transaction.dto.Transaction;
import com.revolut.transaction.exception.TransactionFailedException;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.revolut.common.constant.SqlQueryConstants.*;

public class TransactionDaoImpl implements TransactionDao {
    
    private static Logger log = Logger.getLogger(TransactionDaoImpl.class);
    
    private static final BigDecimal zeroAmount = new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN);
    
    /**
     * Transfer Balance between two accounts.
     * @param userTransaction
     * @return Number of row updated
     */
    @Override
    public int transferAccountBalance(Transaction userTransaction) {
        int result = -1;
        Connection conn = null;
        PreparedStatement lockStatement = null;
        PreparedStatement updateStatement = null;
        ResultSet rs = null;
        
        try {
            conn = DatabaseUtils.getConnection();
            conn.setAutoCommit(false);
            
            // lock the credit and debit account for writing:
            lockStatement = conn.prepareStatement(SQL_LOCK_ACC_BY_ID);
            
            Account fromAccount = getSourceAccountInformation(userTransaction, lockStatement);
            
            lockStatement = conn.prepareStatement(SQL_LOCK_ACC_BY_ID);
            
            Account toAccount = getDestinationAccountInformation(userTransaction, lockStatement);
            
            // check transaction currency
            if (!fromAccount.getCurrencyCode().equals(userTransaction.getCurrencyCode())) {
                throw new AccountException(
                        "Fail to transfer Fund,transaction currency are different from source/destination");
            }
            
            // check ccy is the same for both accounts
            if (!fromAccount.getCurrencyCode().equals(toAccount.getCurrencyCode())) {
                throw new AccountException(
                        "Fail to transfer Fund, the source and destination account are in different currency");
            }
            
            // check enough fund in source account
            BigDecimal sourceAccountAvailableBalance = fromAccount.getBalance().subtract(userTransaction.getAmount());
            if (sourceAccountAvailableBalance.compareTo(zeroAmount) < 0) {
                throw new NotSufficientBalanceException();
            }
            // proceed with update
            updateStatement = conn.prepareStatement(SQL_UPDATE_ACC_BALANCE);
            
            result = updateAccountBalance(updateStatement, sourceAccountAvailableBalance, userTransaction, toAccount);
            
            log.debug("Number of rows updated for the transfer : " + result);
            // If there is no error, commit the transaction
            conn.commit();
        } catch (SQLException se) {
            // rollback transaction if exception occurs
            log.error("User Transaction Failed, rollback initiated for: " + userTransaction, se);
            this.rollBackTransaction(conn);
            
        } catch (NotSufficientBalanceException ex) {
            throw ex;
        } finally {
            DbUtils.closeQuietly(conn);
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(lockStatement);
            DbUtils.closeQuietly(updateStatement);
        }
        return result;
    }
    
    /**
     * Transaction Rollback
     * @param conn
     */
    private void rollBackTransaction(Connection conn) {
        if (conn != null) {
            try {
                conn.rollback();
            } catch (SQLException re) {
                throw new AccountException("Fail to rollback transaction", re);
            }
        }
        
        throw new TransactionFailedException();
    }
    
    /**
     * Get the Source Account (fromAccount) Information.
     * @param transaction
     * @param preparedStatement
     * @return
     * @throws SQLException
     */
    private Account getSourceAccountInformation(Transaction transaction, PreparedStatement preparedStatement) throws SQLException {
        Account fromAccount = new Account();
        
        preparedStatement.setLong(1, transaction.getSourceAccount());
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            fromAccount.setAccountId(rs.getLong("AccountId"));
            fromAccount.setBalance(rs.getBigDecimal("Balance"));
            fromAccount.setUsername(rs.getString("UserName"));
            fromAccount.setCurrencyCode(rs.getString("CurrencyCode"));
            log.debug("transferAccountBalance from Account: " + fromAccount);
        }
        return fromAccount;
        
    }
    
    /**
     * Get the Destination Account (toAccount) Information.
     * @param transaction
     * @param preparedStatement
     * @return
     * @throws SQLException
     */
    private Account getDestinationAccountInformation(Transaction transaction, PreparedStatement preparedStatement) throws SQLException {
        Account toAccount = new Account();
        preparedStatement.setLong(1, transaction.getDestinationAccount());
        ResultSet rs = preparedStatement.executeQuery();
        if (rs.next()) {
            toAccount.setAccountId(rs.getLong("AccountId"));
            toAccount.setBalance(rs.getBigDecimal("Balance"));
            toAccount.setUsername(rs.getString("UserName"));
            toAccount.setCurrencyCode(rs.getString("CurrencyCode"));
            log.debug("transferAccountBalance from Account: " + toAccount);
        }
        return toAccount;
        
    }
    
    /**
     * Update the Account balance between two accounts.
     * @param updateStatement
     * @param sourceAccountAvailableBalance
     * @param userTransaction
     * @param toAccount
     * @return
     * @throws SQLException
     */
    private int updateAccountBalance(PreparedStatement updateStatement, BigDecimal sourceAccountAvailableBalance, Transaction userTransaction, Account toAccount) throws SQLException {
        updateStatement.setBigDecimal(1, sourceAccountAvailableBalance);
        updateStatement.setLong(2, userTransaction.getSourceAccount());
        updateStatement.addBatch();
        updateStatement.setBigDecimal(1, toAccount.getBalance().add(userTransaction.getAmount()));
        updateStatement.setLong(2, userTransaction.getDestinationAccount());
        updateStatement.addBatch();
        int[] rowsUpdated = updateStatement.executeBatch();
        return rowsUpdated[0] + rowsUpdated[1];
    }
    
}
