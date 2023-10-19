package com.group2.oop.account;

import java.util.Objects;

public record User(String username, String firstName, String lastName) {
	public User {
		Objects.requireNonNull(username);
		Objects.requireNonNull(firstName);
		Objects.requireNonNull(lastName);
	}
}