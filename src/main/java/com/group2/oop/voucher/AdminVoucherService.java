package com.group2.oop.voucher;

import com.group2.oop.account.UserRepository;
import com.group2.oop.admin.AdminService;
import com.group2.oop.console.Console;
import com.group2.oop.dependency.D;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import java.util.Scanner;

public class AdminVoucherService implements Service {

	private final UserRepository userRepository = D.get(UserRepository.class);
	private final Scanner scanner = D.get(Scanner.class);
	private final VoucherManager manager = D.get(VoucherManager.class);

	private final Service next;

	public AdminVoucherService() {
		this.next = new AdminService();
	}

	public AdminVoucherService(Service next) {
		this.next = next;
	}

	private void printVouchers(Voucher[] vouchers) {
		System.out.println("---");
		for (int i = 0; i < vouchers.length; i++) {
			var voucher = vouchers[i];
			var claimant = userRepository.get(voucher.claimant().get());
			System.out.println(
				(i + 1) +
				". " +
				voucher.name() +
				"\t : " +
				voucher.price() +
				"\t - " +
				(
					claimant == null || claimant.uuid() == Voucher.NO_CLAIMANT
						? "unclaimed"
						: ("claimed by " + claimant.email())
				) +
				"\t - " +
				(voucher.redeemed().get() ? "redeemed" : "redeemable")
			);
		}
		System.out.println("---");
	}

	@Override
	public void init(Engine engine) {
		main:for (;;) {
			System.out.println(
				"------------------- Manage vouchers -------------------"
			);
			var vouchers = manager.allEveryone().toArray(Voucher[]::new);
			System.out.println("Select a voucher to manage:");
			printVouchers(vouchers);
			System.out.println("");
			System.out.println("99. Create new voucher");
			System.out.println("");
			System.out.println("0. Back");

			int choice;
			mainChoice:for (;;) {
				System.out.print("> ");
				try {
					choice = Integer.parseInt(scanner.nextLine());
				} catch (NumberFormatException e) {
					System.out.println("Invalid choice.");
					continue mainChoice;
				}

				if (choice == 0) {
					System.out.println("[Back]\n");
					break main;
				}

				if (choice == 99) {
					System.out.println("[Create new voucher]\n");
					System.out.println("Enter voucher code:");
					System.out.print("> ");
					var id = scanner.nextLine();
					System.out.println("Enter voucher name:");
					System.out.print("> ");
					var name = scanner.nextLine();
					System.out.println("Enter voucher description:");
					System.out.print("> ");
					var description = scanner.nextLine();
					System.out.println("Enter voucher price:");
					double newVoucherPrice;
					price:for (;;) {
						System.out.print("> ");
						try {
							newVoucherPrice =
								Double.parseDouble(scanner.nextLine());
						} catch (NumberFormatException e) {
							System.out.println("Invalid price.");
							continue price;
						}

						break price;
					}

					var voucher = new Voucher(
						id,
						name,
						description,
						newVoucherPrice
					);
					manager.addNoClaimant(voucher);
					System.out.println("Voucher added successfully!");
					System.out.println("Code:\t" + voucher.id());
					System.out.println("Name:\t" + voucher.name());
					System.out.println("Price:\t" + voucher.price());
					System.out.println(
						"Description:\t" + voucher.description()
					);

					Console.waitForEnter();
					continue main;
				}

				if (choice < 0 || choice > vouchers.length) {
					System.out.println("Invalid choice.");
					continue mainChoice;
				}

				var voucher = vouchers[choice - 1];

				System.out.println(
					"[" + choice + ". " + voucher.name() + "]\n"
				);
				System.out.println("Select an action:");
				System.out.println("1. Edit voucher");
				System.out.println("2. Delete voucher");
				System.out.println("");
				System.out.println("0. Back");

				int actionChoice;
				actionChoice:for (;;) {
					System.out.print("> ");
					try {
						actionChoice = Integer.parseInt(scanner.nextLine());
					} catch (NumberFormatException e) {
						System.out.println("Invalid action choice.");
						continue actionChoice;
					}

					switch (actionChoice) {
						case 1:
							System.out.println("[Edit voucher]\n");
							System.out.println(
								"Enter voucher code [leave blank to skip]:"
							);
							System.out.print("> ");
							var id = scanner.nextLine();
							System.out.println(
								"Enter voucher name [leave blank to skip]:"
							);
							System.out.print("> ");
							var name = scanner.nextLine();
							System.out.println(
								"Enter voucher description [leave blank to skip]:"
							);
							System.out.print("> ");
							var description = scanner.nextLine();
							System.out.println(
								"Enter voucher price [leave blank to skip]:"
							);
							double newVoucherPrice;
							price:for (;;) {
								System.out.print("> ");
								try {
									var newVoucherPriceString = scanner.nextLine();
									if (newVoucherPriceString.length() == 0) {
										newVoucherPrice = Double.NaN;
										break price;
									}
									newVoucherPrice =
										Double.parseDouble(
											newVoucherPriceString
										);
								} catch (NumberFormatException e) {
									System.out.println("Invalid price.");
									continue price;
								}

								break price;
							}

							var newVoucher = new Voucher(
								id.length() == 0 ? voucher.id() : id,
								name.length() == 0 ? voucher.name() : name,
								description.length() == 0
									? voucher.description()
									: description,
								Double.isNaN(newVoucherPrice)
									? voucher.price()
									: newVoucherPrice,
								voucher.claimant().get()
							);
							manager.remove(voucher.claimant().get(), voucher);
							manager.add(
								newVoucher.claimant().get(),
								newVoucher
							);
							System.out.println("Voucher edited successfully!");
							System.out.println("Code:\t" + newVoucher.id());
							System.out.println("Name:\t" + newVoucher.name());
							System.out.println("Price:\t" + newVoucher.price());
							System.out.println(
								"Description:\t" + newVoucher.description()
							);

							Console.waitForEnter();
							continue main;
						case 2:
							System.out.println("[Delete voucher]\n");
							manager.remove(voucher.claimant().get(), voucher);
							System.out.println("Voucher deleted successfully!");

							Console.waitForEnter();
							continue main;
						case 0:
							System.out.println("[Back]\n");
							break main;
						default:
							System.out.println("Invalid action choice.");
							continue actionChoice;
					}
				}
			}
		}

		engine.swap(next);
	}

	@Override
	public void exit() {}
}
