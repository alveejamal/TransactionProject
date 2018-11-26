package com.test.service;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.anyString;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.joda.time.LocalDate;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Matchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;

import com.backbase.model.Transaction;
import com.backbase.model.Transactions;
import com.backbase.service.TransactionService;
import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

public class TransactionServiceTest {

	private static final String ACCOUNT_ID = "savings-kids-john";
	private static final int LENGTH = 10;
	private static final String SORT = "transactionAmount";
	private static final int FROM = 2;
	private static final LocalDate TO_DATE = new LocalDate("2017-10-09");
	private static final LocalDate FROM_DATE = new LocalDate("2016-10-09");
	private static final Double FROM_AMOUNT = 0.0;
	private static final Double TO_AMOUNT = 50.0;
	private static final String TYPE = "sandbox-payment";
	private static final Double TOTAL_AMOUNT = 10.01;

	@Mock
	private RestTemplate restTemplate;

	@InjectMocks
	private TransactionService transactionService;

	@Before
	public void setup() throws JsonParseException, JsonMappingException, IOException {
		MockitoAnnotations.initMocks(this);

		ObjectMapper mapper = new ObjectMapper();
		mapper.enable(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT);

		Transaction t1 = new Transaction();
		Transaction t2 = new Transaction();

		t1.setAccountId("savings-kids-john");
		t1.setCounterpartyAccount("savings-kids-john");
		t1.setCounterpartyLogoPath(null);
		t1.setCounterpartyName("ALIAS_4DF326");
		t1.setDescription("Description abc");
		t1.setId("06ffa118-7892-45c7-8904-f938766680dd");
		t1.setInstructedAmount("0.01");
		t1.setInstructedCurrency("GBP");
		t1.setTransactionAmount(0.01);
		t1.setTransactionCurrency("GBP");
		t1.setTransactionType("sandbox-payment");

		t2.setAccountId("savings-kids-john");
		t2.setCounterpartyAccount("savings-kids-john");
		t2.setCounterpartyLogoPath(null);
		t2.setCounterpartyName("ALIAS_4DF326");
		t2.setDescription("Description abc");
		t2.setId("dcb8138c-eb88-404a-981d-d4edff1086a6");
		t2.setInstructedAmount("10.00");
		t2.setInstructedCurrency("GBP");
		t2.setTransactionAmount(10.0);
		t2.setTransactionCurrency("GBP");
		t2.setTransactionType("sandbox-payment");

		Transactions transactions = new Transactions();

		transactions.setTransactions(Arrays.asList(t1, t2));

		ResponseEntity<Transactions> responseEntity = ResponseEntity.ok().body(transactions);

		when(restTemplate.exchange(anyString(), eq(HttpMethod.GET), Matchers.<HttpEntity<Object>>any(),
				Matchers.<ParameterizedTypeReference<Transactions>>any()

		)).thenReturn(responseEntity);
	}

	@Test
	public void TestGetTransactionsBetweenGivenAmounts() {

		Transactions result = transactionService.getTransactions(ACCOUNT_ID, LENGTH, SORT, FROM, null, null,
				FROM_AMOUNT, TO_AMOUNT, null, null);
		assertEquals(
				result.getTransactions().stream()
						.filter(transaction -> transaction.getTransactionAmount() > FROM_AMOUNT
								&& transaction.getTransactionAmount() < TO_AMOUNT)
						.collect(Collectors.toList()).size(),
				2);
	}

	@Test
	public void TestGetTransactionsBetweenGivenDates() {

		Transactions result = transactionService.getTransactions(ACCOUNT_ID, LENGTH, SORT, FROM, FROM_DATE, TO_DATE,
				null, null, null, null);
		assertEquals(result.getTransactions().size(), 2);

	}

	@Test
	public void TestGetSortedTransactionsBasedOnAmount() {

		Transactions result = transactionService.getTransactions(ACCOUNT_ID, LENGTH, SORT, FROM, null, null,
				FROM_AMOUNT, TO_AMOUNT, null, null);
		assertEquals(result.getTransactions().get(0).getTransactionAmount(), Double.valueOf("0.01"));
	}

	@Test
	public void TestGetTransactionsFilteredByType() {
		Transactions result = transactionService.getFilteredTransactions(TYPE, ACCOUNT_ID);
		assertEquals(
				result.getTransactions().stream().filter(transaction -> transaction.getTransactionType().equals(TYPE))
						.collect(Collectors.toList()).size(),
				2);
	}

	@Test
	public void TestGetTotalAmountForType() {
		Double ans = transactionService.getTotalAmountForType(TYPE, ACCOUNT_ID);
		assertEquals(ans, TOTAL_AMOUNT);
	}

}
