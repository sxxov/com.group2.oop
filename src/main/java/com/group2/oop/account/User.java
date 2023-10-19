package com.group2.oop.account;

import java.util.Objects;
import java.util.UUID;

public record User(
	UUID uuid,
	String email,
	String token,
	String firstName,
	String lastName
) {
	public User {
		Objects.requireNonNull(uuid);
		Objects.requireNonNull(email);
		Objects.requireNonNull(token);
		Objects.requireNonNull(firstName);
		Objects.requireNonNull(lastName);
	}

	public User(String email, String token, String firstName, String lastName) {
		this(UUID.randomUUID(), email, token, firstName, lastName);
	}
}
