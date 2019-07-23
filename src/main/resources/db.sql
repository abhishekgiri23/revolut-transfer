
 drop table IF EXISTS Account;

 create table Account(AccountId LONG PRIMARY KEY AUTO_INCREMENT NOT NULL,
 UserName VARCHAR(30),
 Balance DECIMAL(20,4),
 CurrencyCode VARCHAR(30)
 );

 create UNIQUE INDEX idx_acc on Account(UserName,CurrencyCode);

 insert into Account (AccountId,UserName,Balance,CurrencyCode) values (1,'gilly',1000.00,'GBP');
 insert into Account (AccountId,UserName,Balance,CurrencyCode) values (2,'harry',2000.00,'GBP');
 insert into Account (AccountId,UserName,Balance,CurrencyCode) values (3,'vikas',3000.00,'GBP');
 insert into Account (AccountId,UserName,Balance,CurrencyCode) values (4,'gagan',4000.00,'GBP');
 insert into Account (AccountId,UserName,Balance,CurrencyCode) values (5,'stoke',5000.00,'GBP');
  insert into Account (AccountId,UserName,Balance,CurrencyCode) values (6,'john',000.00,'GBP');




