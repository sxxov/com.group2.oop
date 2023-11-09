package com.group2.oop.carbon_credits;

import com.group2.oop.db.Effect;
import com.group2.oop.store.Store;
import java.io.Serializable;
import java.util.UUID;

public class CarbonCreditAccount implements Serializable {

	private static final long serialVersionUID = 1L;

	@Effect
	private Store<Double> balance;

	public Store<Double> amount() {
		return balance;
	}

	private UUID owner;

	public UUID owner() {
		return owner;
	}

	public CarbonCreditAccount(UUID owner) {
		this(owner, 0);
	}

	public CarbonCreditAccount(UUID owner, double amount) {
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
