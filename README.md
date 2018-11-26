##Developer Info

Ensure the Java Compiler is compatible for Java 8, and the server is Tomcat 9. 

URL - http://localhost:8080/TransactionProject/

##Usage Info

Use browser/eclipse for the best interactive experience.
Login - admin/admin

##Documentation

###Get Transactions Filtered by given parameters

**URL** : TransactionProject/v1/current-accounts/{accountId}/transactions

**Method** : `GET`

**Auth required** : YES

**Path Variables** : accountId

**Re	quest Parameters** :

value = "length", required = true - Number of transactions to be returned(was unsure what this value meant)  
value = "sort", required = true  - The field to be sorted by(was unable to implement this. only sorts by 'transactionAmount')  
value = "from", required = true  - Which index to return the transactions from (was unsure what this field meant)  
value = "fromDate" - Earliest transaction date  
value = "toDate" - Latest transaction date  
value = "fromAmount" - Lowest transaction amount  
value = "toAmount" - Highest transaction amount   
value = "counterpartyAccountNumber" - Counterparty account id   
value = "query" - String inside description and counter party name  

**Sample Request** :  

``` 
http://localhost:8080/TransactionProject/v1/current-accounts/savings-kids-john/transactions?length=10&from=2&sort=transactionAmount&fromDate=2016-10-09&toDate=2017-10-09&fromAmount=0&toAmount=50&query=D
```


**Sample Response** :

```{  
   "transactions":[  
      {  
         "id":"06ffa118-7892-45c7-8904-f938766680dd",
         "accountId":"savings-kids-john",
         "counterpartyAccount":"savings-kids-john",
         "counterpartyName":"ALIAS_4DF326",
         "counterpartyLogoPath":null,
         "instructedAmount":"0.01",
         "instructedCurrency":"GBP",
         "transactionAmount":0.01,
         "transactionCurrency":"GBP",
         "transactionType":"sandbox-payment",
         "description":"Description abc"
      },
      {  
         "id":"dcb8138c-eb88-404a-981d-d4edff1086a6",
         "accountId":"savings-kids-john",
         "counterpartyAccount":"savings-kids-john",
         "counterpartyName":"ALIAS_4DF326",
         "counterpartyLogoPath":null,
         "instructedAmount":"10.00",
         "instructedCurrency":"GBP",
         "transactionAmount":10.0,
         "transactionCurrency":"GBP",
         "transactionType":"sandbox-payment",
         "description":"Description abc"
      }
   ]
} 
```

###Get Transactions By Type


**URL** : TransactionProject/v1/current-accounts/savings-kids-john/getFilteredTransactions

**Method** : `GET`

**Auth required** : YES

**Path Variables** : accountId

**Re	quest Parameters** :

value="type", required = true

**Sample Request** :


``` 
http://localhost:8080/TransactionProject/v1/current-accounts/savings-kids-john/getFilteredTransactions?type=sandbox-payment 
```

**Sample Response ** : 

```
{  
   "transactions":[  
      {  
         "id":"dcb8138c-eb88-404a-981d-d4edff1086a6",
         "accountId":"savings-kids-john",
         "counterpartyAccount":"savings-kids-john",
         "counterpartyName":"ALIAS_4DF326",
         "counterpartyLogoPath":null,
         "instructedAmount":"10.00",
         "instructedCurrency":"GBP",
         "transactionAmount":10.0,
         "transactionCurrency":"GBP",
         "transactionType":"sandbox-payment",
         "description":"Description abc"
      },
      {  
         "id":"06ffa118-7892-45c7-8904-f938766680dd",
         "accountId":"savings-kids-john",
         "counterpartyAccount":"savings-kids-john",
         "counterpartyName":"ALIAS_4DF326",
         "counterpartyLogoPath":null,
         "instructedAmount":"0.01",
         "instructedCurrency":"GBP",
         "transactionAmount":0.01,
         "transactionCurrency":"GBP",
         "transactionType":"sandbox-payment",
         "description":"Description abc"
      }
 }
 ```
      
###Get Total Transaction Amount For Type


**URL** : TransactionProject/v1/current-accounts/savings-kids-john/getTotalAmountForType

**Method** : `GET`

**Auth required** : YES

**Path Variables** : accountId

**Re	quest Parameters** :

value="type", required = true

**Sample Request** :

```
http://localhost:8080/TransactionProject/v1/current-accounts/savings-kids-john/getTotalAmountForType?type=sandbox-payment  
```
**Sample Response ** : 

73.76 

**Reference** :

 
https://github.com/alveejamal/BlogsJ/(my personal github account)

https://github.com/BranislavLazic/SpringSecurityTutorial(For spring security)



