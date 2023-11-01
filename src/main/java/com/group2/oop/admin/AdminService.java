package com.group2.oop.admin;

import com.group2.oop.account.AccountManager;
import com.group2.oop.account.AccountService;
import com.group2.oop.account.UserRole;
import com.group2.oop.console.Console;
import com.group2.oop.dependency.D;
import com.group2.oop.form.ImageForm;
import com.group2.oop.form.ImageFormManager;
import com.group2.oop.home.HomeService;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import com.group2.oop.voucher.AdminVoucherService;
import java.util.Scanner;

public class AdminService implements Service {

	public final AccountManager account = D.get(AccountManager.class);
	private final Scanner scanner = D.get(Scanner.class);
	private final ImageFormManager imageFormManager = new ImageFormManager();

	private final Service next;

	public AdminService() {
		this.next = new HomeService();
	}

	public AdminService(Service next) {
		this.next = next;
	}

	private void printImageForms(ImageForm[] imageForms) {
		System.out.println("---");
		for (int i = 0; i < imageForms.length; i++) {
			var image = imageForms[i];
			System.out.println(
				(i + 1) +
				". " +
				image.submitter().email() +
				"\t : " +
				image.src() +
				"\t - " +
				image.status().get()
			);
		}
		System.out.println("---");
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
			System.out.println("1. Approve an image");
			System.out.println("2. Reject an image");
			System.out.println("3. Show all pending images");
			System.out.println("4. Show all approved images");
			System.out.println("5. Show all rejected images");
			System.out.println("6. Show all images");
			System.out.println("7. Manage vouchers");
			System.out.println("");
			System.out.println("0. Exit");
			System.out.print("> ");

			try {
				choice = Integer.parseInt(scanner.nextLine());
			} catch (NumberFormatException e) {
				System.out.println("Invalid choice.");
				continue;
			}

			switch (choice) {
				case 1:
					System.out.println("[Approve an image]\n");
					var approvedCount = 0;
					for (;;) {
						var approvableImages = imageFormManager
							.allEveryonePending()
							.toArray(new ImageForm[0]);

						if (approvableImages.length == 0) {
							System.out.println("No more images to approve.");
							break;
						}

						printImageForms(approvableImages);
						System.out.println("");
						System.out.println("0. Exit");
						System.out.println(
							"Enter the number of the image to approve: "
						);
						System.out.print("> ");
						int i;

						try {
							i = Integer.parseInt(scanner.nextLine());
						} catch (NumberFormatException e) {
							System.out.println("Invalid number.");
							continue;
						}

						if (i == 0) break;

						if (i < 1 || i > approvableImages.length) {
							System.out.println(
								"Invalid number. It must be between 1-" +
								approvableImages.length +
								"."
							);
							continue;
						}

						imageFormManager.approve(
							approvableImages[i - 1].submitter().uuid(),
							approvableImages[i - 1].src()
						);
						System.out.println("Image approved.");
						System.out.println("");
						++approvedCount;
					}
					System.out.println(approvedCount + " images approved.");
					Console.waitForEnter();
					engine.reload();
					return;
				case 2:
					System.out.println("[Reject an image]\n");
					var rejectCount = 0;
					for (;;) {
						var rejectableImages = imageFormManager
							.allEveryonePending()
							.toArray(new ImageForm[0]);

						if (rejectableImages.length == 0) {
							System.out.println("No more images to reject.");
							break;
						}

						printImageForms(rejectableImages);
						System.out.println("");
						System.out.println("0. Exit");
						System.out.println(
							"Enter the number of the image to reject: "
						);
						System.out.print("> ");
						int i;

						try {
							i = Integer.parseInt(scanner.nextLine());
						} catch (NumberFormatException e) {
							System.out.println("Invalid number.");
							continue;
						}

						if (i == 0) break;

						if (i < 1 || i > rejectableImages.length) {
							System.out.println(
								"Invalid number. It must be between 1-" +
								rejectableImages.length +
								"."
							);
							continue;
						}

						imageFormManager.reject(
							rejectableImages[i - 1].submitter().uuid(),
							rejectableImages[i - 1].src()
						);
						System.out.println("Image rejected.");
						System.out.println("");
						++rejectCount;
					}
					System.out.println(rejectCount + " images rejected.");
					Console.waitForEnter();
					engine.reload();
					return;
				case 3:
					System.out.println("[Show all pending images]\n");
					printImageForms(
						imageFormManager
							.allEveryonePending()
							.toArray(new ImageForm[0])
					);
					Console.waitForEnter();
					engine.reload();
					return;
				case 4:
					System.out.println("[Show all approved images]\n");
					printImageForms(
						imageFormManager
							.allEveryoneApproved()
							.toArray(new ImageForm[0])
					);
					Console.waitForEnter();
					engine.reload();
					return;
				case 5:
					System.out.println("[Show all rejected images]\n");
					printImageForms(
						imageFormManager
							.allEveryoneRejected()
							.toArray(new ImageForm[0])
					);
					Console.waitForEnter();
					engine.reload();
					return;
				case 6:
					System.out.print("[Show all images]\n");
					printImageForms(
						imageFormManager.allEveryone().toArray(new ImageForm[0])
					);
					Console.waitForEnter();
					engine.reload();
					return;
				case 7:
					System.out.print("[Manage vouchers]\n");
					engine.swap(new AdminVoucherService(this));
					return;
				case 0:
					System.out.println("[Exit]\n");
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
