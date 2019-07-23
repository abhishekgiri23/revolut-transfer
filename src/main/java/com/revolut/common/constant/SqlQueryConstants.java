package com.revolut.common.constant;

public class SqlQueryConstants {
    
    public final static String CREATE_ACCOUNT = "INSERT INTO Account (UserName, Balance, CurrencyCode) VALUES (?, ?, ?)";
    public final static String SQL_GET_ACC_BY_ID = "SELECT * FROM Account WHERE AccountId = ? ";
    public final static String SQL_LOCK_ACC_BY_ID = "SELECT * FROM Account WHERE AccountId = ? FOR UPDATE";
    public final static String SQL_CREATE_ACC = "INSERT INTO Account (UserName, Balance, CurrencyCode) VALUES (?, ?, ?)";
    public final static String SQL_UPDATE_ACC_BALANCE = "UPDATE Account SET Balance = ? WHERE AccountId = ? ";
    public final static String SQL_GET_ALL_ACC = "SELECT * FROM Account";
    public final static String SQL_DELETE_ACC_BY_ID = "DELETE FROM Account WHERE AccountId = ?";
    public final static String SQL_INSERT_TRANSACTION = "INSERT INTO Transaction (Status,Amount,SourceAccount,DestinationAccount,CurrencyCode) VALUES (?,?,?,?,?)";
}
