package com.group2.oop.account;

import com.group2.oop.db.Repository;
import com.group2.oop.db.drill.MapDrill0;
import com.group2.oop.db.drill.MapDrill1;
import java.util.Optional;
import java.util.UUID;

public class UserRepository extends Repository<UUID, User> {

	public MapDrill0<UUID, User> drill(UUID k) {
		return new MapDrill1<>(this).drill(k);
	}

	public Optional<User> get(String email) {
		return values()
			.stream()
			.filter(u -> u.email().equals(email))
			.findFirst();
	}
}
