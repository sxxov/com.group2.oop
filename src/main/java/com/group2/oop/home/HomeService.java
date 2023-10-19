package com.group2.oop.home;

import com.group2.oop.account.AccountManager;
import com.group2.oop.account.AccountService;
import com.group2.oop.dependency.D;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import java.util.Scanner;

public class HomeService implements Service {

	public final AccountManager account = D.get(AccountManager.class);
	public final Scanner scanner = D.get(Scanner.class);

	@Override
	public void init(Engine engine) {
		System.out.println("------------------- Home -------------------");
		var user = account.current().get();
		System.out.println("Welcome, " + user.firstName() + "!");

		for (;;) {
			System.out.println("1. Logout");
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

				break;
			}

			switch (choice) {
				case 1:
					account.logout();
					engine.swap(new AccountService());

					return;
				case 0:
					engine.exit();

					return;
				default:
					System.out.println("Invalid choice.");

					continue;
			}
		}
	}

	@Override
	public void exit() {}
}
