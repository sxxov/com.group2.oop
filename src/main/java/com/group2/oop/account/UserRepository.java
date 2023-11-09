package com.group2.oop.account;

import com.group2.oop.db.Repository;
import com.group2.oop.db.drill.MapDrill0;
import com.group2.oop.db.drill.MapDrill1;
import java.util.UUID;
import javax.annotation.Nullable;

public class UserRepository extends Repository<UUID, User> {

	private static final long serialVersionUID = 1L;

	public MapDrill0<UUID, User> drill(UUID k) {
		return new MapDrill1<>(this).drill(k);
	}

	public @Nullable User get(String email) {
		return values()
			.stream()
			.filter(u -> u.email().equals(email))
			.findFirst()
			.orElse(null);
	}

	public @Nullable User get(UUID uuid) {
		return super.get(uuid);
	}
}
