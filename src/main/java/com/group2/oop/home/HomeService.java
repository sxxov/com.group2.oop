package com.group2.oop.home;

import com.group2.oop.account.AccountManager;
import com.group2.oop.account.AccountService;
import com.group2.oop.dependency.D;
import com.group2.oop.form.ImageFormService;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import com.group2.oop.voucher.UserVoucherService;
import java.util.Scanner;

public class HomeService implements Service {

	public final AccountManager account = D.get(AccountManager.class);
	public final Scanner scanner = D.get(Scanner.class);

	@Override
	public void init(Engine engine) {
		if (account.current().isEmpty()) {
			engine.swap(new AccountService());
			return;
		}

		System.out.println("------------------- Home -------------------");
		var user = account.current().get();
		System.out.println("Welcome, " + user.firstName() + "!");

		for (;;) {
			System.out.println("1. Your images");
			System.out.println("2. Your vouchers");
			System.out.println("");
			System.out.println("0. Logout & Exit");

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
					System.out.println("[Your images]\n");
					engine.swap(new ImageFormService());

					return;
				case 2:
					System.out.println("[Your vouchers]\n");
					engine.swap(new UserVoucherService());

					return;
				case 0:
					System.out.println("[Logout & Exit]\n");
					account.logout();
					engine.swap(new AccountService());

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
