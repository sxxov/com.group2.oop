package com.group2.oop.account;

import com.group2.oop.serialisation.MapIO;
import java.io.IOException;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

public class UserRepository {

	private static final String USER_REPOSITORY_PATH = "users.ser";

	private HashMap<UUID, User> users = new HashMap<>();
	private MapIO<UUID, User> io = new MapIO<>(users, USER_REPOSITORY_PATH);

	public UserRepository() {
		try {
			users = io.read();
		} catch (IOException e) {
			System.out.println("Error reading users from disk.");
			e.printStackTrace();
		}
	}

	public void add(User user) {
		users.put(user.uuid(), user);
		write();
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
		write();
	}

	public void remove(User user) {
		remove(user.uuid());
	}

	private void write() {
		try {
			io.write();
		} catch (IOException e) {
			System.out.println("Error writing users.");
			e.printStackTrace();
		}
	}
}
