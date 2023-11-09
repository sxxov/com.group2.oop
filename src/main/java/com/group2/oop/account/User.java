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
    UserRole role,
    double carbonCredits  // New field for carbon credits
) implements Serializable {
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
        this(UUID.randomUUID(), email, token, firstName, lastName, role, 0.0); // Default carbon credits
    }

    public User(String email, String token, String firstName, String lastName) {
        this(
            UUID.randomUUID(),
            email,
            token,
            firstName,
            lastName,
            UserRole.USER,
            0.0  // Default carbon credits
        );
    }

    // Getter and Setter for carbon credits
    public double getCarbonCredits() {
        return carbonCredits;
    }

    public User setCarbonCredits(double carbonCredits) {
        return new User(uuid, email, token, firstName, lastName, role, carbonCredits);
    }
}
