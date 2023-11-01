package com.group2.oop.account;

import com.group2.oop.admin.AdminHomeService;
import com.group2.oop.dependency.D;
import com.group2.oop.home.HomeService;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import java.util.Optional;
import java.util.Scanner;

public class AccountService implements Service {

	private final Scanner scanner = D.get(Scanner.class);
	private final AccountManager account = D.get(AccountManager.class);

	private final Optional<Service> next;

	public AccountService() {
		this.next = Optional.empty();
	}

	public AccountService(Service next) {
		this.next = Optional.of(next);
	}

	@Override
	public void init(Engine engine) {
		account.logout();

		System.out.println("------------------- Account -------------------");
		System.out.println("1. Login");
		System.out.println("2. Register");
		System.out.println("");
		System.out.println("0. Exit");

		int choice;
		for (;;) {
			System.out.print("> ");

			try {
				choice = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Invalid choice.");

				continue;
			}

			// TODO: don't store passwords in heap, use char[] instead
			String email, password;
			switch (choice) {
				case 1:
					System.out.println("[Login]\n");
					for (;;) {
						// email
						System.out.println("Enter your email:");
						for (;;) {
							System.out.print("> ");
							email = scanner.nextLine();

							if (!AccountManager.isValidEmail(email)) {
								System.out.println(
									"Email must follow the format: <username>@<domain>"
								);
								continue;
							}

							break;
						}
						// password
						System.out.println("Enter your password:");
						for (;;) {
							System.out.print("> ");
							password = scanner.nextLine();

							break;
						}

						var u = account.login(email, password.toCharArray());
						if (u.isEmpty()) {
							System.out.println("Invalid email or password!");

							engine.reload();
							return;
						}

						break;
					}
					System.out.println("Logged in successfully.");

					break;
				case 2:
					System.out.println("[Register]\n");
					for (;;) {
						// email
						System.out.println("Enter your email:");
						for (;;) {
							System.out.print("> ");
							email = scanner.nextLine();

							if (!AccountManager.isValidEmail(email)) {
								System.out.println(
									"Email must follow the format: <username>@<domain>"
								);
								continue;
							}

							break;
						}
						// password
						System.out.println("Enter your password:");
						for (;;) {
							System.out.print("> ");
							password = scanner.nextLine();

							if (
								!AccountManager.isValidPassword(
									password.toCharArray()
								)
							) {
								System.out.println(
									"Password must be at least 8 characters long and contain at least one number, one lowercase letter, one uppercase letter, and one symbol."
								);
								continue;
							}

							break;
						}
						// first name
						System.out.println("Enter your first name:");
						System.out.print("> ");
						var firstName = scanner.nextLine();
						// last name
						System.out.println("Enter your last name:");
						System.out.print("> ");
						var lastName = scanner.nextLine();

						var role = UserRole.USER;
						int roleIndex;
						System.out.println("Pick your role:");
						System.out.println("1. User");
						System.out.println("2. Admin");
						for (;;) {
							System.out.print("> ");

							try {
								roleIndex =
									Integer.parseInt(scanner.nextLine());
							} catch (NumberFormatException e) {
								System.out.println("Invalid role.");

								continue;
							}

							switch (roleIndex) {
								case 1:
									role = UserRole.USER;
									break;
								case 2:
									role = UserRole.ADMIN;
									break;
								default:
									System.out.println("Invalid role.");

									continue;
							}

							break;
						}

						try {
							account.register(
								email,
								password.toCharArray(),
								firstName,
								lastName,
								role
							);
						} catch (
							InvalidEmailException | InvalidPasswordException e
						) {
							throw new IllegalStateException(e);
						}

						break;
					}
					System.out.println("Registered successfully.");

					break;
				case 0:
					System.out.println("[Exit]\n");
					engine.exit();
					return;
				default:
					System.out.println("Invalid choice.");
					continue;
			}

			break;
		}

		engine.swap(
			next.isPresent()
				? next.get()
				: account.current().isPresent()
					? account.current().get().role() == UserRole.ADMIN
						? new AdminHomeService()
						: new HomeService()
					: new AccountService()
		);
	}

	@Override
	public void exit() {}
}
