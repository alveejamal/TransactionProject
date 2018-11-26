package com.backbase.sorters;

import java.util.Comparator;

import com.backbase.model.Transaction;

public class AmountSorter implements Comparator<Transaction>{

	@Override
	public int compare(Transaction o1, Transaction o2) {
		if(o1.getTransactionAmount() < o2.getTransactionAmount())
			return -1;
		if (o1.getTransactionAmount()> o2.getTransactionAmount())
			return 1;
		else 
			return 0;
	}

}
