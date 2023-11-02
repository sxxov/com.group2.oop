package com.group2.oop.carbon_credits;

import com.group2.oop.dependency.D;
import java.util.UUID;
import javax.annotation.Nullable;

public class CarbonCreditAccountManager {

	private final CarbonCreditAccountRepository repository = D.get(
		CarbonCreditAccountRepository.class
	);

	public @Nullable CarbonCreditAccount account(UUID uuid) {
		return repository.drill(uuid).drill();
	}
}
