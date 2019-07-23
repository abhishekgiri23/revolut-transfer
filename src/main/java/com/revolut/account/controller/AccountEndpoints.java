package com.revolut.account.controller;

import com.revolut.account.dao.AccountFactory;
import com.revolut.account.dao.impl.AccountDaoImpl;
import com.revolut.account.dto.Account;
import com.revolut.common.utils.CommonUtils;
import org.apache.log4j.Logger;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/account")
@Produces(MediaType.APPLICATION_JSON)
public class AccountEndpoints {
    
    private static Logger LOG = Logger.getLogger(AccountEndpoints.class);
    
    
    AccountDaoImpl accountDao = AccountFactory.getAccountDao();
    
    
    @GET
    @Path("/{id}")
    public Account getAccount(@PathParam("id") long accountId) {
        return accountDao.getAccountById(accountId);
    }
    
    
    @POST
    @Path("/create")
    public Response createAccount(Account account) {
        final long accountId = accountDao.createAccount(account);
        LOG.debug(accountId);
        return Response.status(Response.Status.CREATED).build();
    }
    
    @PUT
    @Path("/deposit")
    public Response deposit(Account account) {
        
        if (account.getBalance().compareTo(CommonUtils.zeroAmount) <= 0) {
            throw new WebApplicationException("Invalid Deposit amount", Response.Status.BAD_REQUEST);
        }
        
        accountDao.updateAccountBalance(account);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
    
    
    @PUT
    @Path("/withdraw")
    public Response withdraw(Account account) {
        
        if (account.getBalance().compareTo(CommonUtils.zeroAmount) <= 0) {
            throw new WebApplicationException("Invalid Deposit amount", Response.Status.BAD_REQUEST);
        }
        if (LOG.isDebugEnabled())
            LOG.debug("Withdraw service: change to account  " + " Account ID = " + account.getAccountId());
        accountDao.updateAccountBalance(account);
        return Response.status(Response.Status.NO_CONTENT).build();
    }
}
