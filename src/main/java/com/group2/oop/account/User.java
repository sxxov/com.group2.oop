package com.group2.oop.account;

import java.util.Objects;
import java.util.UUID;

public record User(
	UUID uuid,
	String username,
	String firstName,
	String lastName
) {
	public User {
		Objects.requireNonNull(uuid);
		Objects.requireNonNull(username);
		Objects.requireNonNull(firstName);
		Objects.requireNonNull(lastName);
	}

	public User(String username, String firstName, String lastName) {
		this(UUID.randomUUID(), username, firstName, lastName);
	}
}
