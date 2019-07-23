package com.revolut;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.revolut.account.controller.AccountEndpoints;
import com.revolut.common.errors.ApplicationExceptionMapper;
import com.revolut.common.utils.DatabaseUtils;
import com.revolut.transaction.controller.TransactionEndpoints;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.HttpClientUtils;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.impl.conn.PoolingHttpClientConnectionManager;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;
import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.util.concurrent.TimeUnit;

public class EndpointTest {
    protected static Server server = null;
    protected static PoolingHttpClientConnectionManager connManager = new PoolingHttpClientConnectionManager();
    
    protected static HttpClient client;
    protected ObjectMapper mapper = new ObjectMapper();
    protected URIBuilder builder = new URIBuilder().setScheme("http").setHost("localhost:8084");
    
    
    @BeforeClass
    public static void setup() throws Exception {
        DatabaseUtils.populateData();
        startServer();
        connManager.setDefaultMaxPerRoute(1000);
        connManager.setMaxTotal(50);
        connManager.setValidateAfterInactivity(1000);
        client = HttpClients.custom()
                .setConnectionManager(connManager)
                .setConnectionTimeToLive(1000, TimeUnit.MILLISECONDS)
                .setConnectionManagerShared(true)
                .setRetryHandler((exception, executionCount, context) -> {
                    if (executionCount > 3) {
                        return false;
                    }
                    if (exception instanceof org.apache.http.NoHttpResponseException) {
                        return true;
                    }
                    return false;
                })
                .build();
        
    }
    
    @AfterClass
    public static void closeClient() throws Exception {
        //server.stop();
        HttpClientUtils.closeQuietly(client);
    }
    
    
    private static void startServer() throws Exception {
        if (server == null) {
            server = new Server(8084);
            ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
            context.setContextPath("/");
            server.setHandler(context);
            ServletHolder servletHolder = context.addServlet(ServletContainer.class, "/*");
            servletHolder.setInitParameter("jersey.config.server.provider.classnames",
                    AccountEndpoints.class.getCanonicalName() +
                            "," + ApplicationExceptionMapper.class.getCanonicalName() +
                            "," + TransactionEndpoints.class.getCanonicalName());
            server.start();
        }
    }
    
}
