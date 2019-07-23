package com.revolut.account;

import com.revolut.EndpointTest;
import com.revolut.account.dto.Account;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.util.EntityUtils;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertTrue;

public class AccountEndpointTest extends EndpointTest {
    
    @Test
    public void testCreateAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/create").build();
        System.out.println(uri.toString());
        String jsonInString = mapper.writeValueAsString(AccountTestData.buildNewAccountData());
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 201);
    }
    
    @Test
    public void testGetAccountByUserName() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/2").build();
        HttpGet request = new HttpGet(uri);
        request.setHeader("Content-type", "application/json");
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
       assertTrue(statusCode == 200);
        //check the content
        String jsonString = EntityUtils.toString(response.getEntity());
        Account account = mapper.readValue(jsonString, Account.class);
        
        assertTrue(account.getUsername().equals("harry"));
    }
    
    @Test
    public void testCreateExistingAccount() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/create").build();
        System.out.println(uri.toString());
        String jsonInString = mapper.writeValueAsString(AccountTestData.buildWithdrawAccountDataWithInvalidBalance());
        StringEntity entity = new StringEntity(jsonInString);
        HttpPost request = new HttpPost(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 500);
    }
    
    @Test
    public void testDeposit() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/deposit").build();
        String jsonInString = mapper.writeValueAsString(AccountTestData.buildCreateAccountData());
        StringEntity entity = new StringEntity(jsonInString);
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 204);
        
    }
    
    
    @Test
    public void testWithdraw() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/withdraw").build();
        String jsonInString = mapper.writeValueAsString(AccountTestData.buildCreateAccountData());
        StringEntity entity = new StringEntity(jsonInString);
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 204);
        
    }
    
    @Test
    public void testWithdrawWithInvalidFund() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/withdraw").build();
        String jsonInString = mapper.writeValueAsString(AccountTestData.buildWithdrawAccountDataWithInvalidBalance());
        StringEntity entity = new StringEntity(jsonInString);
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 500);
        
    }
    
    @Test
    public void testDepositWithInvalidFund() throws IOException, URISyntaxException {
        URI uri = builder.setPath("/account/deposit").build();
        String jsonInString = mapper.writeValueAsString(AccountTestData.buildWithdrawAccountDataWithInvalidBalance());
        StringEntity entity = new StringEntity(jsonInString);
        HttpPut request = new HttpPut(uri);
        request.setHeader("Content-type", "application/json");
        request.setEntity(entity);
        HttpResponse response = client.execute(request);
        int statusCode = response.getStatusLine().getStatusCode();
        assertTrue(statusCode == 500);
        
    }
    
}
