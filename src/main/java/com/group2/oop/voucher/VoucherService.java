package com.group2.oop.voucher;

import com.group2.oop.account.AccountManager;
import com.group2.oop.account.AccountService;
import com.group2.oop.carbon_credits.CarbonCreditAccount;
import com.group2.oop.carbon_credits.CarbonCreditAccountRepository;
import com.group2.oop.console.Console;
import com.group2.oop.dependency.D;
import com.group2.oop.home.HomeService;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import java.util.Scanner;

public class VoucherService implements Service {

	public final AccountManager account = D.get(AccountManager.class);
	public final Scanner scanner = D.get(Scanner.class);

	private final VoucherRepository repository = D.get(VoucherRepository.class);
	private final CarbonCreditAccountRepository carbonCredit = D.get(
		CarbonCreditAccountRepository.class
	);

	private final Service next;

	public VoucherService() {
		this.next = new HomeService();
	}

	public VoucherService(Service next) {
		this.next = next;
	}

	private void printVouchers(Voucher[] vouchers) {
		System.out.println("---");
		for (int i = 0; i < vouchers.length; i++) {
			var voucher = vouchers[i];
			System.out.println(
				(i + 1) +
				". " +
				voucher.name() +
				"\t : " +
				voucher.price() +
				"\t - " +
				(voucher.redeemed().get() ? "redeemed" : "available") +
				"\t - " +
				voucher.description()
			);
		}
		System.out.println("---");
	}

	@Override
	public void init(Engine engine) {
		if (account.current().isEmpty()) {
			engine.swap(new AccountService());
			return;
		}

		main:for (;;) {
			System.out.println(
				"------------------- Vouchers -------------------"
			);
			System.out.println("1. Shop vouchers");
			System.out.println("2. Redeem a voucher");
			System.out.println("3. History");
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

				switch (choice) {
					case 1:
						// View voucher listings
						System.out.println("[Shop vouchers]\n");
						var shopVouchers = repository
							.drill(Voucher.NO_CLAIMANT)
							.ref()
							.values()
							.toArray(Voucher[]::new);

						if (shopVouchers.length <= 0) {
							System.out.println(
								"There are no vouchers available at the moment. Try again later."
							);

							Console.waitForEnter();
							continue main;
						}

						shopVoucher:for (;;) {
							System.out.println("Pick a voucher to claim:");
							printVouchers(shopVouchers);
							System.out.println("");
							System.out.println("0. Exit");

							int voucherChoice;
							shopVoucherChoice:for (;;) {
								System.out.print("> ");

								try {
									voucherChoice =
										Integer.parseInt(scanner.nextLine());
								} catch (NumberFormatException e) {
									System.out.println(
										"Invalid voucher choice."
									);

									continue shopVoucherChoice;
								}

								if (voucherChoice == 0) continue main;

								if (
									voucherChoice < 0 ||
									voucherChoice > shopVouchers.length
								) {
									System.out.println(
										"Invalid voucher choice."
									);

									continue shopVoucherChoice;
								}

								var voucher = shopVouchers[voucherChoice - 1];

								if (voucher.claimed()) {
									System.out.println(
										"This voucher has already been claimed."
									);

									Console.waitForEnter();
									continue shopVoucher;
								}

								var price = voucher.price();
								var credit = carbonCredit
									.drill(account.current().get().uuid())
									.orElsePut(v ->
										new CarbonCreditAccount(
											account.current().get()
										)
									);

								if (price > credit.amount().get()) {
									System.out.println(
										"You don't have enough credits to redeem this voucher. " +
										price +
										" credits are required, whilst you only have" +
										credit.amount().get() +
										"."
									);

									Console.waitForEnter();
									continue shopVoucher;
								}

								credit.deduct(price);

								repository
									.drill(Voucher.NO_CLAIMANT)
									.ref()
									.remove(voucher.id());
								repository
									.drill(account.current().get().uuid())
									.ref()
									.put(voucher.id(), voucher);
								voucher.claim(account.current().get());

								System.out.println(
									"Voucher claimed successfully! " +
									price +
									" credits were deducted. You now have " +
									credit.amount().get() +
									" credits left."
								);

								Console.waitForEnter();
								continue main;
							}
						}
					case 2:
						System.out.println("[Redeem a voucher]\n");

						var ownedVouchers = repository
							.drill(account.current().get().uuid())
							.ref()
							.values()
							.toArray(Voucher[]::new);

						if (ownedVouchers.length <= 0) {
							System.out.println(
								"You don't have any vouchers yet. Purchase some at the shop."
							);

							Console.waitForEnter();
							continue main;
						}

						redeemVoucher:for (;;) {
							System.out.println("Pick a voucher to redeem:");
							printVouchers(ownedVouchers);
							System.out.println("");
							System.out.println("0. Exit");

							int voucherChoice;
							redeemVoucherChoice:for (;;) {
								System.out.print("> ");

								try {
									voucherChoice =
										Integer.parseInt(scanner.nextLine());
								} catch (NumberFormatException e) {
									System.out.println(
										"Invalid voucher choice."
									);

									continue redeemVoucherChoice;
								}

								if (voucherChoice == 0) continue main;

								if (
									voucherChoice < 0 ||
									voucherChoice > ownedVouchers.length
								) {
									System.out.println(
										"Invalid voucher choice."
									);

									continue redeemVoucherChoice;
								}

								var voucher = ownedVouchers[voucherChoice - 1];

								if (voucher.redeemed().get()) {
									System.out.println(
										"This voucher has already been redeemed."
									);

									Console.waitForEnter();
									continue redeemVoucher;
								}

								voucher.redeem();

								System.out.println(
									"Voucher redeemed successfully!"
								);

								Console.waitForEnter();
								continue main;
							}
						}
					case 3:
						System.out.println("[History]\n");
						var pastVouchers = repository
							.drill(account.current().get().uuid())
							.ref()
							.values()
							.toArray(Voucher[]::new);

						if (pastVouchers.length <= 0) {
							System.out.println(
								"You don't have any vouchers yet. Purchase some at the shop."
							);

							Console.waitForEnter();
							continue main;
						}

						printVouchers(pastVouchers);
						Console.waitForEnter();
						continue main;
					case 0:
						System.out.println("[Exit]\n");
						break main;
					default:
						System.out.println("Invalid choice.");
				}
			}
		}

		engine.swap(next);
	}

	@Override
	public void exit() {}
}
