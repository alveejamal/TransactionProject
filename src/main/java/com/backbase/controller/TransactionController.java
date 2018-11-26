package com.backbase.controller;

import org.slf4j.LoggerFactory;
import org.joda.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.slf4j.Logger;

import com.backbase.model.Transactions;
import com.backbase.service.TransactionService;

@Controller
@RequestMapping("/v1/current-accounts/{accountId}/")
public class TransactionController {

	private static final Logger LOG = LoggerFactory.getLogger(TransactionController.class);

	private static final String DATE_PATTERN_YEAR_MONTH_DATE = "yyyy-MM-dd";

	@Autowired
	private TransactionService transactionService;

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "transactions", produces = "application/json")
	@ResponseBody
	public Transactions getTransactions(@PathVariable("accountId") String accountId,
			@RequestParam(value = "length", required = true) int length,
			@RequestParam(value = "sort", required = true) String sort,
			@RequestParam(value = "from", required = true) int from,
			@RequestParam(value = "fromDate", required = false) @DateTimeFormat(pattern = DATE_PATTERN_YEAR_MONTH_DATE) LocalDate fromDate,
			@RequestParam(value = "toDate", required = false) @DateTimeFormat(pattern = DATE_PATTERN_YEAR_MONTH_DATE) LocalDate toDate,
			@RequestParam(value = "fromAmount", required = false) Double fromAmount,
			@RequestParam(value = "toAmount", required = false) Double toAmount,
			@RequestParam(value = "counterpartyAccountNumber", required = false) String cpAccount,
			@RequestParam(value = "query", required = false) String query) {
		LOG.info("Request sent for getTransactions");
		return transactionService.getTransactions(accountId, length, sort, from, fromDate, toDate, fromAmount, toAmount,
				cpAccount, query);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "getFilteredTransactions", produces = "application/json")
	@ResponseBody
	public Transactions getFilteredTransactions(@PathVariable("accountId") String accountId,
			@RequestParam(value = "type", required = true) String type) {
		LOG.info("Request sent for getFilteredTransactions");
		return transactionService.getFilteredTransactions(type, accountId);
	}

	@PreAuthorize("hasRole('ROLE_ADMIN')")
	@RequestMapping(value = "getTotalAmountForType")
	@ResponseBody
	public Double getTotalAmountForType(@PathVariable("accountId") String accountId,
			@RequestParam(value = "type", required = true) String type) {
		LOG.info("Request sent for getTotalAmountForType");
		return transactionService.getTotalAmountForType(type, accountId);
	}
}
