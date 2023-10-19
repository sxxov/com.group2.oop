package com.group2.oop.account;

import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class UserRepository {

	private static HashMap<UUID, User> users = new HashMap<>();

	public static void add(User user) {
		users.put(user.uuid(), user);
	}

	public static Optional<User> get(UUID uuid) {
		return Optional.ofNullable(users.get(uuid));
	}

	public static void remove(UUID uuid) {
		users.remove(uuid);
	}

	public static void remove(User user) {
		users.remove(user.uuid());
	}
}
