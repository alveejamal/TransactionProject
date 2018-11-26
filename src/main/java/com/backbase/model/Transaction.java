package com.backbase.model;

import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Transaction {

	private String id;

	private String accountId;

	private String counterpartyAccount;

	private String counterpartyName;

	private String counterpartyLogoPath;

	private String instructedAmount;

	private String instructedCurrency;

	private Double transactionAmount;

	private String transactionCurrency;

	private String transactionType;

	private String description;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getAccountId() {
		return accountId;
	}

	public void setAccountId(String accountId) {
		this.accountId = accountId;
	}

	public String getCounterpartyAccount() {
		return counterpartyAccount;
	}

	public void setCounterpartyAccount(String counterpartyAccount) {
		this.counterpartyAccount = counterpartyAccount;
	}

	public String getCounterpartyName() {
		return counterpartyName;
	}

	public void setCounterpartyName(String counterpartyName) {
		this.counterpartyName = counterpartyName;
	}

	public String getCounterpartyLogoPath() {
		return counterpartyLogoPath;
	}

	public void setCounterpartyLogoPath(String counterpartyLogoPath) {
		this.counterpartyLogoPath = counterpartyLogoPath;
	}

	public String getInstructedAmount() {
		return instructedAmount;
	}

	public void setInstructedAmount(String instructedAmount) {
		this.instructedAmount = instructedAmount;
	}

	public String getInstructedCurrency() {
		return instructedCurrency;
	}

	public void setInstructedCurrency(String instructedCurrency) {
		this.instructedCurrency = instructedCurrency;
	}

	public Double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(Double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public String getTransactionCurrency() {
		return transactionCurrency;
	}

	public void setTransactionCurrency(String transactionCurrency) {
		this.transactionCurrency = transactionCurrency;
	}

	public String getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(String transactionType) {
		this.transactionType = transactionType;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	@JsonProperty("this_account")
	private void getNestedAccount(Map<String, Object> this_account) {
		this.accountId = (String) this_account.get("id");
	}

	@JsonProperty("other_account")
	private void getNestedOtherAccount(Map<String, Object> other_account) {
		this.counterpartyAccount = (String) other_account.get("number");
		@SuppressWarnings("unchecked")
		Map<String, Object> holder = (Map<String, Object>) other_account.get("holder");
		@SuppressWarnings("unchecked")
		Map<String, Object> metadata = (Map<String, Object>) other_account.get("metadata");

		this.counterpartyName = (String) holder.get("name");
		this.counterpartyLogoPath = (String) metadata.get("image_URL");

	}

	@JsonProperty("details")
	private void getNestedDetails(Map<String, Object> details) {
		@SuppressWarnings("unchecked")
		Map<String, Object> value = (Map<String, Object>) details.get("value");

		this.instructedAmount = (String) value.get("amount");
		this.instructedCurrency = (String) value.get("currency");
		this.transactionAmount = Double.parseDouble(this.instructedAmount);
		this.transactionCurrency = this.instructedCurrency;
		this.transactionType = (String) details.get("type") == null ? "null" : (String) details.get("type");
		this.description = (String) details.get("description");
	}
}
