package com.group2.oop.carbon_credits;

import com.group2.oop.account.AccountManager;
import com.group2.oop.console.Console;
import com.group2.oop.dependency.D;
import com.group2.oop.home.HomeService;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import java.util.Scanner;

public class CarbonCreditService implements Service {

	private final Scanner scanner = D.get(Scanner.class);
	private final AccountManager account = D.get(AccountManager.class);
	private final CarbonCreditAccountRepository carbon = D.get(
		CarbonCreditAccountRepository.class
	);

	private final Service next;

	public CarbonCreditService() {
		this.next = new HomeService();
	}

	public CarbonCreditService(Service next) {
		this.next = next;
	}

	@Override
	public void init(Engine engine) {
		var carbonAccount = carbon.get(account.current().get().uuid());

		main:for (;;) {
			System.out.println(
				"------------------- Your carbon credits -------------------"
			);
			System.out.println("1. Check balance");
			System.out.println("");
			System.out.println("0. Exit");

			int choice;
			mainChoice:for (;;) {
				System.out.print("> ");

				try {
					choice = Integer.parseInt(scanner.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Invalid choice.");
					continue;
				}

				switch (choice) {
					case 1:
						System.out.println("[Check balance]\n");
						System.out.println(
							"Your current balance is: " +
							carbonAccount.amount().get()
						);
						Console.waitForEnter();
						continue main;
					case 0:
						System.out.println("[Exit]\n");
						break main;
					default:
						System.out.println("Invalid choice");
						continue mainChoice;
				}
			}
		}

		engine.swap(next);
	}

	@Override
	public void exit() {}
}
