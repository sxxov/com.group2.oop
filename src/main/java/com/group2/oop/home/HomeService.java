package com.group2.oop.home;

import com.group2.oop.account.AccountManager;
import com.group2.oop.account.AccountService;
import com.group2.oop.account.UserRole;
import com.group2.oop.admin.AdminService;
import com.group2.oop.carbon_credits.CarbonCreditService;
import com.group2.oop.dependency.D;
import com.group2.oop.form.ImageFormService;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import com.group2.oop.voucher.VoucherService;
import com.group2.oop.rank.RankingSystem;
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

		main:for (;;) {
			System.out.println("------------------- Home -------------------");
			var user = account.current().get();
			System.out.println("Welcome, " + user.firstName() + "!");

			System.out.println("1. Your images");
			System.out.println("2. Your carbon credits");
			System.out.println("3. Your vouchers");
      System.out.println("4. User ranking");
			System.out.println("");
			System.out.println("0. Logout & Exit");
			if (user.role() == UserRole.ADMIN) {
				System.out.println("99. Enter admin panel");
			}

			int choice;
			mainChoice:for (;;) {
				System.out.print("> ");

				try {
					choice = Integer.parseInt(scanner.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Invalid choice.");

					continue mainChoice;
				}

				switch (choice) {
					case 1:
						System.out.println("[Your images]\n");
						engine.swap(new ImageFormService());

						break main;
					case 2:
						System.out.println("[Your carbon credits]\n");
						engine.swap(new CarbonCreditService());

						break main;
					case 3:
						System.out.println("[Your vouchers]\n");
						engine.swap(new VoucherService());

						break main;
          case 4:
            System.out.println("[User ranking]\n");
            engine.swap(new RankingSystem());

            break main;
					case 0:
						System.out.println("[Logout & Exit]\n");
						account.logout();
						engine.swap(new AccountService());

						break main;
					case 99:
						if (user.role() != UserRole.ADMIN) {
							System.out.println("Invalid choice.");

							continue mainChoice;
						}

						System.out.println("[Enter admin panel]\n");
						engine.swap(new AdminService(this));

						break main;
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
