package com.group2.oop.account;

import com.group2.oop.dependency.D;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.IntStream;

public class AccountManager {

	private final UserRepository userRepository = D.get(UserRepository.class);

	private Optional<User> current = Optional.empty();

	public Optional<User> current() {
		return current;
	}

	public void logout() {
		current = Optional.empty();
	}

	public Optional<User> login(String email, char[] password) {
		var u = userRepository.get(email);
		if (u.isEmpty()) {
			return current = Optional.empty();
		}

		var user = u.get();
		var auth = new PasswordAuth();
		if (auth.authenticate(password, user.token())) {
			return current = Optional.of(user);
		} else {
			return current = Optional.empty();
		}
	}

	public User register(
		String email,
		char[] password,
		String firstName,
		String lastName,
		UserRole role
	) throws InvalidEmailException, InvalidPasswordException {
		if (!isValidEmail(email)) throw new InvalidEmailException();
		if (!isValidPassword(password)) throw new InvalidPasswordException();

		var auth = new PasswordAuth();
		var token = auth.hash(password);
		var user = new User(email, token, firstName, lastName, role);
		userRepository.put(user.uuid(), user);

		current = Optional.of(user);

		return user;
	}

	public User register(
		String email,
		char[] password,
		String firstName,
		String lastName
	) throws InvalidEmailException, InvalidPasswordException {
		return register(email, password, firstName, lastName, UserRole.USER);
	}

	private static Pattern emailPattern = Pattern.compile("^(.+)@(.+)$");

	public static boolean isValidEmail(String email) {
		var matcher = emailPattern.matcher(email);
		return matcher.matches();
	}

	private static Pattern numericPattern = Pattern.compile("^[0-9]+$");
	private static Pattern lowercasePattern = Pattern.compile("^[a-z]+$");
	private static Pattern uppercasePattern = Pattern.compile("^[A-Z]+$");
	private static Pattern symbolPattern = Pattern.compile("^[^a-zA-Z0-9]+$");

	public static boolean isValidPassword(char[] password) {
		if (password.length < 8) return false;

		return (
			IntStream
				.range(0, password.length)
				.mapToObj(i -> password[i])
				.anyMatch(c ->
					lowercasePattern.matcher(String.valueOf(c)).matches()
				) &&
			IntStream
				.range(0, password.length)
				.mapToObj(i -> password[i])
				.anyMatch(c ->
					uppercasePattern.matcher(String.valueOf(c)).matches()
				) &&
			IntStream
				.range(0, password.length)
				.mapToObj(i -> password[i])
				.anyMatch(c ->
					numericPattern.matcher(String.valueOf(c)).matches()
				) &&
			IntStream
				.range(0, password.length)
				.mapToObj(i -> password[i])
				.anyMatch(c ->
					symbolPattern.matcher(String.valueOf(c)).matches()
				)
		);
	}
}
