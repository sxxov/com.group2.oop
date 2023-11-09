package com.group2.oop.carbon_credits;

import com.group2.oop.account.UserRepository;
import com.group2.oop.db.Repository;
import com.group2.oop.db.drill.MapDrill0;
import com.group2.oop.db.drill.MapDrill1;
import com.group2.oop.dependency.D;
import java.util.UUID;
import javax.annotation.Nullable;

public class CarbonCreditAccountRepository
	extends Repository<UUID, CarbonCreditAccount> {

	private static final long serialVersionUID = 1L;

	private transient UserRepository userRepository = D.get(
		UserRepository.class
	);

	private void readObject(java.io.ObjectInputStream in)
		throws java.io.IOException, ClassNotFoundException {
		in.defaultReadObject();
		userRepository = D.get(UserRepository.class);
	}

	public CarbonCreditAccountRepository() {
		super();
		userRepository.registerOnMutated((k, v, m) -> {
			if (k instanceof UUID uuid) {
				if (v == null) {
					remove(uuid);
				} else {
					put(uuid, new CarbonCreditAccount(uuid));
				}
			}
		});
	}

	public MapDrill0<UUID, CarbonCreditAccount> drill(UUID k) {
		return new MapDrill1<>(this).drill(k);
	}

	public CarbonCreditAccount get(UUID uuid) {
		var account = super.get(uuid);

		if (account == null) {
			account = new CarbonCreditAccount(uuid);
			put(uuid, account);
		}

		return account;
	}

	@Override
	public @Nullable CarbonCreditAccount get(Object key) {
		if (!(key instanceof UUID uuid)) return null;

		var account = super.get(uuid);

		if (account == null) {
			account = new CarbonCreditAccount(uuid);
			put(uuid, account);
		}

		return account;
	}
}
