package com.group2.oop.admin;

import com.group2.oop.account.AccountManager;
import com.group2.oop.account.AccountService;
import com.group2.oop.account.UserRepository;
import com.group2.oop.account.UserRole;
import com.group2.oop.dependency.D;
import com.group2.oop.form.AdminImageFormService;
import com.group2.oop.home.HomeService;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import com.group2.oop.voucher.AdminVoucherService;
import java.util.Scanner;

public class AdminService implements Service {

	public final UserRepository userRepository = D.get(UserRepository.class);
	public final AccountManager account = D.get(AccountManager.class);
	private final Scanner scanner = D.get(Scanner.class);

	private final Service next;

	public AdminService() {
		this.next = new HomeService();
	}

	public AdminService(Service next) {
		this.next = next;
	}

	@Override
	public void init(Engine engine) {
		if (
			account.current().isEmpty() ||
			account.current().get().role() != UserRole.ADMIN
		) {
			engine.swap(new AccountService());
			return;
		}

		int choice;
		while (true) {
			System.out.println(
				"------------------- Admin Menu -------------------"
			);
			System.out.println("1. Manage submitted images");
			System.out.println("2. Manage vouchers");
			System.out.println("");
			System.out.println("0. Back");
			System.out.print("> ");

			try {
				choice = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Invalid choice.");
				continue;
			}

			switch (choice) {
				case 1:
					System.out.print("[Manage submitted images]\n");
					engine.swap(new AdminImageFormService(this));
					return;
				case 2:
					System.out.print("[Manage vouchers]\n");
					engine.swap(new AdminVoucherService(this));
					return;
				case 0:
					System.out.println("[Back]\n");
					engine.swap(next);
					return;
				default:
					System.out.println("Invalid choice");
					continue;
			}
		}
	}

	@Override
	public void exit() {}
}
