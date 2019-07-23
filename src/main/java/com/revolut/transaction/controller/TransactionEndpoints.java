package com.revolut.transaction.controller;


import com.revolut.transaction.dao.TransactionDao;
import com.revolut.transaction.dao.TransactionFactory;
import com.revolut.transaction.dto.Transaction;
import org.apache.log4j.Logger;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.Response;

import static com.revolut.common.utils.CommonUtils.validateCurrencyCode;

@Path("/transaction")
@Consumes("application/json")
@Produces("application/json")
public class TransactionEndpoints {
    
    private static Logger LOG = Logger.getLogger(TransactionEndpoints.class);
    private TransactionDao transactionDao = TransactionFactory.getTransactionDao();
    
    @POST
    public Response transferFund(Transaction transaction) {
        String currency = transaction.getCurrencyCode();
        LOG.debug(transaction);
        if (validateCurrencyCode(currency)) {
            int updateCount = transactionDao.transferAccountBalance(transaction);
            if (updateCount == 2) {
                return Response.status(Response.Status.OK).build();
            } else {
                // transaction failed
                throw new WebApplicationException("Transaction failed", Response.Status.INTERNAL_SERVER_ERROR);
            }
            
        } else {
            throw new WebApplicationException("Currency Code Invalid ", Response.Status.BAD_REQUEST);
        }
        
        
    }
    
}
