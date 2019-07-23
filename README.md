# money-transfer

#getAccount GET
localhost:8080/account/3

#transferFund POST
localhost:8080/transaction
```
{  
  "amount":500,
  "sourceAccount":3,
  "destinationAccount":4,
  "currencyCode":"GBP"
} 
```

#deposit PUT
localhost:8080/account/deposit
```
{  
  "username":"gilly",
  "balance":500,
  "accountId":1,
  "currencyCode":"GBP"
} 
```

#createAccount POST
localhost:8080/account/create
```
{  
  "username":"rogan",
  "balance":500,
  "accountId":11,
  "currencyCode":"GBP"
} 
```

#withdraw PUT
localhost:8080/account/withdraw

```
{  
  "username":"gilly",
  "balance":500,
  "accountId":1,
  "currencyCode":"GBP"
} 
```
