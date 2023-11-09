package com.group2.oop.account;

import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

public record User(
	UUID uuid,
	String email,
	String token,
	String firstName,
	String lastName,
	UserRole role
)
	implements Serializable {
	private static final long serialVersionUID = 1L;

	public User {
		Objects.requireNonNull(uuid);
		Objects.requireNonNull(email);
		Objects.requireNonNull(token);
		Objects.requireNonNull(firstName);
		Objects.requireNonNull(lastName);
		Objects.requireNonNull(role);
	}

	public User(
		String email,
		String token,
		String firstName,
		String lastName,
		UserRole role
	) {
		this(UUID.randomUUID(), email, token, firstName, lastName, role);
	}

	public User(String email, String token, String firstName, String lastName) {
		this(
			UUID.randomUUID(),
			email,
			token,
			firstName,
			lastName,
			UserRole.USER
		);
	}
}
