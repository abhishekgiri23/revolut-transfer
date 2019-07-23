package com.revolut.common.errors;

import org.apache.log4j.Logger;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

public class ApplicationExceptionMapper {
    
    private static Logger log = Logger.getLogger(ApplicationExceptionMapper.class);
    
    public ApplicationExceptionMapper() {
    }
    
    public Response toResponse(ApplicationException daoException) {
        if (log.isDebugEnabled()) {
            log.debug("Mapping exception to Response....");
        }
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setErrorCode(daoException.getMessage());
        
        // return internal server error for DAO exceptions
        return Response.status(Response.Status.INTERNAL_SERVER_ERROR).entity(errorResponse).type(MediaType.APPLICATION_JSON).build();
    }
    
}
