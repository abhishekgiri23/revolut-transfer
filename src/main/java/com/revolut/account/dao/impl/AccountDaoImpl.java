package com.revolut.account.dao.impl;

import com.revolut.account.dao.AccountDao;
import com.revolut.account.dto.Account;
import com.revolut.account.exception.AccountException;
import com.revolut.account.exception.AccountNotCreatedException;
import com.revolut.account.exception.NotSufficientBalanceException;
import com.revolut.common.errors.ApplicationException;
import com.revolut.common.utils.DatabaseUtils;
import org.apache.commons.dbutils.DbUtils;
import org.apache.log4j.Logger;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static com.revolut.common.constant.SqlQueryConstants.CREATE_ACCOUNT;
import static com.revolut.common.constant.SqlQueryConstants.SQL_GET_ACC_BY_ID;
import static com.revolut.common.constant.SqlQueryConstants.SQL_LOCK_ACC_BY_ID;
import static com.revolut.common.constant.SqlQueryConstants.SQL_UPDATE_ACC_BALANCE;


public class AccountDaoImpl implements AccountDao {
    
    private static Logger LOG = Logger.getLogger(AccountDaoImpl.class);
    public static final BigDecimal zeroAmount = new BigDecimal(0).setScale(4, RoundingMode.HALF_EVEN);
    
    /**
     * Update the Account Balance
     * @param account
     * @return Number of row updated
     * @throws AccountException
     */
    @Override
    public int updateAccountBalance(Account account) throws AccountException {
        
        Connection connection = null;
        PreparedStatement lockStmt = null;
        PreparedStatement updateStmt = null;
        ResultSet rs = null;
        Account targetAccount = new Account();
        int updateCount = -1;
        try {
            connection = DatabaseUtils.getConnection();
            connection.setAutoCommit(false);
            // lock account for writing:
            lockStmt = connection.prepareStatement(SQL_LOCK_ACC_BY_ID);
            lockStmt.setLong(1, account.getAccountId());
            rs = lockStmt.executeQuery();
            if (rs.next()) {
                targetAccount.setAccountId(rs.getLong("AccountId"));
                targetAccount.setBalance(rs.getBigDecimal("Balance"));
                targetAccount.setUsername(rs.getString("UserName"));
                targetAccount.setCurrencyCode(rs.getString("CurrencyCode"));
                
                if (LOG.isDebugEnabled())
                    LOG.debug("updateAccountBalance from Account: " + targetAccount);
            }
            
            if (targetAccount == null) {
                throw new AccountException("updateAccountBalance(): fail to lock account : " + account.getAccountId());
            }
            // update account upon success locking
            BigDecimal balance = targetAccount.getBalance().add(account.getBalance());
            if (balance.compareTo(zeroAmount) < 0) {
                throw new NotSufficientBalanceException();
            }
            
            updateStmt = connection.prepareStatement(SQL_UPDATE_ACC_BALANCE);
            updateStmt.setBigDecimal(1, balance);
            updateStmt.setLong(2, account.getAccountId());
            updateCount = updateStmt.executeUpdate();
            connection.commit();
            if (LOG.isDebugEnabled())
                LOG.debug("New Balance after Update: " + targetAccount);
            return updateCount;
        } catch (SQLException se) {
            // rollback transaction if exception occurs
            LOG.error(" User Transaction Failed, rollback initiated for: " + account.getAccountId(), se);
            try {
                if (connection != null)
                    connection.rollback();
            } catch (SQLException re) {
                throw new AccountException("Fail to rollback transaction", re);
            }
        } catch (ApplicationException se) {
            LOG.error("Update Account Failed", se);
        } finally {
            DbUtils.closeQuietly(connection);
            DbUtils.closeQuietly(rs);
            DbUtils.closeQuietly(lockStmt);
            DbUtils.closeQuietly(updateStmt);
        }
        return updateCount;
    }
    
    /**
     * Create a Account
     * @param account
     * @return Account ID
     * @throws AccountException
     */
    @Override
    public long createAccount(Account account) throws AccountException {
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet generatedKeys = null;
        
        try {
            connection = DatabaseUtils.getConnection();
            statement = connection.prepareStatement(CREATE_ACCOUNT);
            statement.setString(1, account.getUsername());
            statement.setBigDecimal(2, account.getBalance());
            statement.setString(3, account.getCurrencyCode());
            int numberOfRow = statement.executeUpdate();
            if (numberOfRow == 0) {
                LOG.error("Account Creation failed");
                throw new AccountNotCreatedException();
            }
            generatedKeys = statement.getGeneratedKeys();
            if (generatedKeys.next()) {
                return generatedKeys.getLong(1);
            } else {
                LOG.error("Creating account failed, no ID obtained.");
                throw new AccountNotCreatedException();
            }
        } catch (SQLException e) {
            LOG.error("Error Inserting Account  " + account);
            throw new AccountException("createAccount(): Error creating user account " + account, e);
        } catch (ApplicationException e) {
            LOG.error("Account Create Failed", e);
            throw new AccountException("createAccount(): Error creating user account " + account, e);
        } finally {
            DbUtils.closeQuietly(connection, statement, generatedKeys);
        }
    }
    
    /**
     * Get Account Information by ID
     * @param accountId
     * @return Account information {@link Account}
     * @throws AccountException
     */
    @Override
    public Account getAccountById(long accountId) throws AccountException {
        Connection conn = null;
        PreparedStatement stmt = null;
        ResultSet rs = null;
        Account acc = new Account();
        try {
            conn = DatabaseUtils.getConnection();
            stmt = conn.prepareStatement(SQL_GET_ACC_BY_ID);
            stmt.setLong(1, accountId);
            rs = stmt.executeQuery();
            if (rs.next()) {
                acc.setUsername(rs.getString("UserName"));
                acc.setCurrencyCode(rs.getString("CurrencyCode"));
                acc.setBalance(rs.getBigDecimal("Balance"));
                acc.setAccountId(rs.getLong("AccountId"));
                if (LOG.isDebugEnabled())
                    LOG.debug("Retrieve Account By Id: " + acc);
            }
            return acc;
        } catch (SQLException e) {
            throw new AccountException("getAccountById(): Error reading account data", e);
        } finally {
            DbUtils.closeQuietly(conn, stmt, rs);
        }
        
    }
}
