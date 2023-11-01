package com.group2.oop.carbon_credits;

import com.group2.oop.account.User;
import com.group2.oop.db.Effect;
import com.group2.oop.store.Store;
import java.io.Serializable;

public class CarbonCreditAccount implements Serializable {

	@Effect
	private Store<Double> balance;

	public Store<Double> amount() {
		return balance;
	}

	private User owner;

	public User owner() {
		return owner;
	}

	public CarbonCreditAccount(User owner) {
		this(owner, 0);
	}

	public CarbonCreditAccount(User owner, double amount) {
		this.owner = owner;
		this.balance = new Store<>(amount);
	}

	public void deduct(double amount) {
		balance.set(balance.get() - amount);
	}

	public void deposit(double amount) {
		balance.set(balance.get() + amount);
	}
}
