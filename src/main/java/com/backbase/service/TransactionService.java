package com.backbase.service;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.annotation.Resource;

import org.joda.time.LocalDate;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import com.backbase.model.Transaction;
import com.backbase.model.Transactions;
import com.backbase.sorters.AmountSorter;

/**
 * @author shojamal
 *
 */
@Component
public class TransactionService {
	private static final Logger LOG = LoggerFactory.getLogger(TransactionService.class);

	@Resource(name = "myRestTemplate")
	private RestTemplate restTemplate;

	private String url = "https://apisandbox.openbankproject.com/obp/v1.2.1/banks/rbs/accounts/%s/public/transactions";

	/**
	 * Gets all transaction filtered by the given params
	 * 
	 * @param accountId  id of concerned account
	 * @param length     number of transactions to be returned(was unsure what this
	 *                   value meant)
	 * @param sort       the field to be sorted by(was unable to implement this.
	 *                   only sorts by transactionAmount)
	 * @param from       which index to return the transactions from (was unsure)
	 * @param fromDate   earliest transaction date
	 * @param toDate     latest transaction date
	 * @param fromAmount lowest transaction amount
	 * @param toAmount   highest transaction amount
	 * @param cpAccount  counterparty account id
	 * @param query      string inside description and counter party name
	 * @return List of transactions
	 */
	public Transactions getTransactions(String accountId, int length, String sort, int from, LocalDate fromDate,
			LocalDate toDate, Double fromAmount, Double toAmount, String cpAccount, String query) {

		DateTimeFormatter dtf = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss.SSS'Z");

		Map<String, String> headersMap = new HashMap();

		if (fromDate != null)
			headersMap.put("obp_from_date", fromDate.toDateTimeAtStartOfDay().toString(dtf));

		if (toDate != null)
			headersMap.put("obp_to_date", toDate.toDateTimeAtStartOfDay().toString(dtf));

		headersMap.put("obp_limit", length + "");

		HttpHeaders headers = new HttpHeaders();
		headers.setAll(headersMap);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType((MediaType.APPLICATION_JSON));

		ResponseEntity<Transactions> response = sendRequest(accountId, headers);
		if (response == null)
			return null;
		List<Transaction> transactions = response.getBody().getTransactions();

		if (from < transactions.size())
			transactions = transactions.subList(from, transactions.size() - 1);

		if (fromAmount != null)
			transactions = transactions.stream().filter(transaction -> transaction.getTransactionAmount() > fromAmount)
					.collect(Collectors.toList());

		if (toAmount != null)
			transactions = transactions.stream().filter(transaction -> transaction.getTransactionAmount() < toAmount)
					.collect(Collectors.toList());

		if (cpAccount != null)
			transactions = transactions.stream()
					.filter(transaction -> transaction.getCounterpartyAccount().equals(cpAccount))
					.collect(Collectors.toList());

		if (query != null)
			transactions = transactions.stream().filter(transaction -> transaction.getCounterpartyName().contains(query)
					&& transaction.getDescription().contains(query)).collect(Collectors.toList());

		if (sort.equals("transactionAmount"))
			Collections.sort(transactions, new AmountSorter());

		Transactions transactionAns = new Transactions();
		transactionAns.setTransactions(transactions);
		return transactionAns;
	}

	/**
	 * Filters transactions by given type
	 * 
	 * @param type      type of transaction
	 * @param accountId id of concerned account
	 * @return all transactions of the account of given type
	 */
	public Transactions getFilteredTransactions(String type, String accountId) {
		HttpHeaders headers = new HttpHeaders();
		List<Transaction> transactions = sendRequest(accountId, headers).getBody().getTransactions().stream()
				.filter(transaction -> transaction.getTransactionType().equals(type)).collect(Collectors.toList());

		Transactions transactionAns = new Transactions();
		transactionAns.setTransactions(transactions);
		return transactionAns;
	}

	/**
	 * returns total value of transation of given type of account id.
	 * 
	 * @param type      type of transaction to get total amounts
	 * @param accountId id of concerned account
	 * @return the total amount of all transaction of the given type
	 */
	public Double getTotalAmountForType(String type, String accountId) {
		return getFilteredTransactions(type, accountId).getTransactions().stream()
				.mapToDouble(transaction -> transaction.getTransactionAmount()).sum();
	}

	/**
	 * centralized method to send all 3rd party request
	 * 
	 * @param accountId Account for which the request is being sent
	 * @param headers   List of headers to customize data
	 * @return data regarding account
	 */
	private ResponseEntity<Transactions> sendRequest(String accountId, HttpHeaders headers) {
		url = String.format(url, accountId);
		headers.setAccept(Collections.singletonList(MediaType.APPLICATION_JSON));
		headers.setContentType((MediaType.APPLICATION_JSON));

		ResponseEntity<Transactions> response = null;
		try {
			LOG.info("Sending request to " + url);
			response = restTemplate.exchange(url, HttpMethod.GET, new HttpEntity<Object>(headers),
					new ParameterizedTypeReference<Transactions>() {
					});
		} catch (RestClientException restClientException) {
			LOG.debug("Request failed with exception " + restClientException.getCause().getMessage());
		}
		return response;
	}

}
