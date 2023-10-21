package com.group2.oop.admin;

import com.group2.oop.account.AccountManager;
import com.group2.oop.account.AccountService;
import com.group2.oop.account.UserRole;
import com.group2.oop.console.Console;
import com.group2.oop.dependency.D;
import com.group2.oop.form.ImageForm;
import com.group2.oop.form.ImageFormManager;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import java.util.Scanner;

public class AdminHomeService implements Service {

	public final AccountManager account = D.get(AccountManager.class);
	private final Scanner scanner = D.get(Scanner.class);
	private final ImageFormManager imageFormManager = new ImageFormManager();

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
			System.out.println("2. Show all pending images");
			System.out.println("3. Show all approved images");
			System.out.println("4. Show all rejected images");
			System.out.println("5. Show all images");
			System.out.println("");
			System.out.println("0. Logout & Exit");
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
							.allPending()
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

						imageFormManager.approve(approvableImages[i - 1].src());
						System.out.println("Image approved.");
						System.out.println("");
						++approvedCount;
					}
					System.out.println(approvedCount + " images approved.");
					Console.waitForEnter();
					engine.reload();
					return;
				case 2:
					System.out.println("[Show all pending images]\n");
					printImageForms(
						imageFormManager.allPending().toArray(new ImageForm[0])
					);
					Console.waitForEnter();
					engine.reload();
					return;
				case 3:
					System.out.println("[Show all approved images]\n");
					printImageForms(
						imageFormManager.allApproved().toArray(new ImageForm[0])
					);
					Console.waitForEnter();
					engine.reload();
					return;
				case 4:
					System.out.println("[Show all rejected images]\n");
					printImageForms(
						imageFormManager.allRejected().toArray(new ImageForm[0])
					);
					Console.waitForEnter();
					engine.reload();
					return;
				case 5:
					System.out.print("[Show all images]\n\n");
					printImageForms(
						imageFormManager.all().toArray(new ImageForm[0])
					);
					Console.waitForEnter();
					engine.reload();
					return;
				case 0:
					System.out.println("[Logout & Exit]\n");
					account.logout();
					engine.swap(new AccountService());
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
