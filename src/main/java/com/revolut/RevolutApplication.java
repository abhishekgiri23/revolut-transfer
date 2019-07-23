package com.revolut;

import com.revolut.account.controller.AccountEndpoints;
import com.revolut.common.errors.ApplicationExceptionMapper;
import com.revolut.common.utils.DatabaseUtils;
import com.revolut.transaction.controller.TransactionEndpoints;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;

public class RevolutApplication {
    
    public static void main(String[] args) throws Exception {
        
        DatabaseUtils.populateData();
        startServer();
    }
    
    private static void startServer() throws Exception {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");
        server.setHandler(context);
        ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
        servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                AccountEndpoints.class.getCanonicalName() +
                        "," + ApplicationExceptionMapper.class.getCanonicalName() +
                        "," + TransactionEndpoints.class.getCanonicalName());
        try {
            server.start();
            server.join();
        } finally {
            server.destroy();
        }
        
        
    }
}
