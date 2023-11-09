package com.group2.oop.voucher;

import com.group2.oop.account.User;
import com.group2.oop.db.Effect;
import com.group2.oop.store.Store;
import com.group2.oop.store.Supply;
import java.io.Serializable;
import java.util.UUID;

public class Voucher implements Serializable {

	private static final long serialVersionUID = 1L;

	public static final UUID NO_CLAIMANT = UUID.fromString(
		"00000000-0000-0000-0000-000000000000"
	);

	private String id;
	private String name;
	private String description;
	private double price;

	@Effect
	private Store<Boolean> redeemed;

	@Effect
	private Store<UUID> claimant;

	public Voucher(
		String id,
		String name,
		String description,
		double price,
		boolean redeemed,
		UUID claimant
	) {
		this.id = id;
		this.name = name;
		this.description = description;
		this.price = price;
		this.redeemed = new Store<>(redeemed);
		this.claimant = new Store<>(NO_CLAIMANT);
	}

	public Voucher(
		String id,
		String name,
		String description,
		double price,
		UUID claimant
	) {
		this(id, name, description, price, false, claimant);
	}

	public Voucher(String id, String name, String description, double price) {
		this(id, name, description, price, false, NO_CLAIMANT);
	}

	public String id() {
		return id;
	}

	public String name() {
		return name;
	}

	public String description() {
		return description;
	}

	public Supply<Boolean> redeemed() {
		return redeemed.supply();
	}

	public double price() {
		return price;
	}

	public Supply<UUID> claimant() {
		return claimant.supply();
	}

	public void claim(UUID uuid) {
		claimant.set(uuid);
	}

	public void claim(User user) {
		claimant.set(user.uuid());
	}

	public void unclaim() {
		claimant.set(NO_CLAIMANT);
	}

	public boolean claimed() {
		return !claimant.get().equals(NO_CLAIMANT);
	}

	public void redeem() {
		redeemed.set(true);
	}

	public void unredeem() {
		redeemed.set(false);
	}
}
