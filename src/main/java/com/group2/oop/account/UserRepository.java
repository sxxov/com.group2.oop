package com.group2.oop.account;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class UserRepository {

	private HashMap<UUID, User> users = new HashMap<>();

	public void add(User user) {
		users.put(user.uuid(), user);
	}

	public Optional<User> get(String email) {
		return users
			.values()
			.stream()
			.filter(user -> user.email().equals(email))
			.findFirst();
	}

	public Optional<User> get(UUID uuid) {
		return Optional.ofNullable(users.get(uuid));
	}

	public void remove(UUID uuid) {
		users.remove(uuid);
	}

	public void remove(User user) {
		users.remove(user.uuid());
	}
}
