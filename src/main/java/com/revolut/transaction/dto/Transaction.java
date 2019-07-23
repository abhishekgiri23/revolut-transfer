package com.revolut.transaction.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.math.BigDecimal;


public class Transaction {
    
    private String status;
    
    @JsonProperty(required = true)
    private BigDecimal amount;
    
    @JsonProperty(required = true)
    private Long sourceAccount;
    
    @JsonProperty(required = true)
    private Long destinationAccount;
    
    @JsonProperty(required = true)
    private String currencyCode;
    
    
    public String getStatus() {
        return status;
    }
    
    public void setStatus(String status) {
        this.status = status;
    }
    
    public BigDecimal getAmount() {
        return amount;
    }
    
    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }
    
    public Long getSourceAccount() {
        return sourceAccount;
    }
    
    public void setSourceAccount(Long sourceAccount) {
        this.sourceAccount = sourceAccount;
    }
    
    public Long getDestinationAccount() {
        return destinationAccount;
    }
    
    public void setDestinationAccount(Long destinationAccount) {
        this.destinationAccount = destinationAccount;
    }
    
    public String getCurrencyCode() {
        return currencyCode;
    }
    
    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }
}
