package com.group2.oop.account;

import com.group2.oop.console.Console;
import com.group2.oop.dependency.D;
import com.group2.oop.home.HomeService;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import java.util.Scanner;

public class AccountService implements Service {

	private final Scanner scanner = D.get(Scanner.class);
	private final AccountManager account = D.get(AccountManager.class);

	private final Service next;

	public AccountService() {
		this.next = new HomeService();
	}

	public AccountService(Service next) {
		this.next = next;
	}

	@Override
	public void init(Engine engine) {
		account.logout();

		main:for (;;) {
			System.out.println(
				"------------------- Account -------------------"
			);
			System.out.println("1. Login");
			System.out.println("2. Register");
			System.out.println("");
			System.out.println("0. Exit");

			int choice;
			mainChoice:for (;;) {
				System.out.print("> ");

				try {
					choice = Integer.parseInt(scanner.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Invalid choice.");

					continue mainChoice;
				}

				// TODO: don't store passwords in heap, use char[] instead
				switch (choice) {
					case 1:
						System.out.println("[Login]\n");
						// email
						System.out.println("Enter your email:");

						String loginEmail, loginPassword;
						email:for (;;) {
							System.out.print("> ");
							loginEmail = scanner.nextLine();

							if (!AccountManager.isValidEmail(loginEmail)) {
								System.out.println(
									"Email must follow the format: <username>@<domain>"
								);
								continue email;
							}

							if (!account.exists(loginEmail)) {
								System.out.println("Email does not exist.");
								Console.waitForEnter();

								continue main;
							}

							break email;
						}
						// password
						System.out.println("Enter your password:");
						System.out.print("> ");
						loginPassword = scanner.nextLine();

						var u = account.login(
							loginEmail,
							loginPassword.toCharArray()
						);
						if (u.isEmpty()) {
							System.out.println("Invalid email or password.");
							Console.waitForEnter();

							continue main;
						}

						System.out.println("Logged in successfully.\n");

						engine.swap(next);
						return;
					case 2:
						System.out.println("[Register]\n");
						// email
						System.out.println("Enter your email:");
						String registerEmail, registerPassword;
						email:for (;;) {
							System.out.print("> ");
							registerEmail = scanner.nextLine();

							if (!AccountManager.isValidEmail(registerEmail)) {
								System.out.println(
									"Email must follow the format: <username>@<domain>"
								);
								continue email;
							}

							if (account.exists(registerEmail)) {
								System.out.println(
									"Email already exists. Login instead."
								);
								Console.waitForEnter();

								continue main;
							}

							break;
						}
						// password
						System.out.println("Enter your password:");
						password:for (;;) {
							System.out.print("> ");
							registerPassword = scanner.nextLine();

							if (
								!AccountManager.isValidPassword(
									registerPassword.toCharArray()
								)
							) {
								System.out.println(
									"Password must be at least 8 characters long and contain at least one number, one lowercase letter, one uppercase letter, and one symbol."
								);
								continue password;
							}

							break password;
						}
						// first name
						System.out.println("Enter your first name:");
						System.out.print("> ");
						var registerFirstName = scanner.nextLine();
						// last name
						System.out.println("Enter your last name:");
						System.out.print("> ");
						var registerLastName = scanner.nextLine();

						var registerRole = UserRole.USER;
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
									registerRole = UserRole.USER;
									break;
								case 2:
									registerRole = UserRole.ADMIN;
									break;
								default:
									System.out.println("Invalid role.");

									continue;
							}

							break;
						}

						try {
							account.register(
								registerEmail,
								registerPassword.toCharArray(),
								registerFirstName,
								registerLastName,
								registerRole
							);
						} catch (
							InvalidEmailException
							| InvalidPasswordException
							| UserAlreadyExistsException e
						) {
							throw new IllegalStateException(e);
						}

						System.out.println("Registered successfully.\n");

						engine.swap(next);
						return;
					case 0:
						System.out.println("[Exit]\n");
						engine.exit();
						return;
					default:
						System.out.println("Invalid choice.");
						continue mainChoice;
				}
			}
		}
	}

	@Override
	public void exit() {}
}
