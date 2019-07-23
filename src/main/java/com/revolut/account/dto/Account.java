package com.revolut.account.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;



public class Account {
    
    @JsonProperty(required = true)
    private String username;
    
    @JsonProperty(required = true)
    private BigDecimal balance;
    
    @JsonProperty(required = true)
    private Long accountId;
    
    @JsonProperty(required = true)
    private String currencyCode;
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public BigDecimal getBalance() {
        return balance;
    }
    
    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }
    
    public Long getAccountId() {
        return accountId;
    }
    
    public void setAccountId(Long accountId) {
        this.accountId = accountId;
    }
    
    public String getCurrencyCode() {
        return currencyCode;
    }
    
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
