package com.group2.oop.carbon_credits;

import com.group2.oop.db.Repository;
import com.group2.oop.db.drill.MapDrill0;
import com.group2.oop.db.drill.MapDrill1;
import java.util.UUID;

public class CarbonCreditAccountRepository
	extends Repository<UUID, CarbonCreditAccount> {

	public MapDrill0<UUID, CarbonCreditAccount> drill(UUID k) {
		return new MapDrill1<>(this).drill(k);
	}
}
