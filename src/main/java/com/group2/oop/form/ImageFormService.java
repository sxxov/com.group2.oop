package com.group2.oop.form;

import com.group2.oop.account.AccountService;
import com.group2.oop.account.UnauthorisedException;
import com.group2.oop.console.Console;
import com.group2.oop.dependency.D;
import com.group2.oop.home.HomeService;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import java.util.Scanner;

public class ImageFormService implements Service {

	private final Scanner scanner = D.get(Scanner.class);
	private ImageFormManager manager = D.get(ImageFormManager.class);

	private final Service next;

	public ImageFormService() {
		this.next = new HomeService();
	}

	public ImageFormService(Service next) {
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
		System.out.println(
			"------------------- Your images -------------------"
		);
		System.out.println("1. Submit new image");
		System.out.println("2. History");
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

			switch (choice) {
				case 1:
					System.out.println("[Submit new image]\n");
					System.out.println("Enter the image filepath:");
					System.out.print("> ");

					var filepath = scanner.nextLine();
					try {
						manager.submit(filepath);
					} catch (UnauthorisedException e) {
						engine.swap(new AccountService(this));
						return;
					}

					System.out.println(
						"Successfully submitted image. Await approval within 24 hours!"
					);
					Console.waitForEnter();
					engine.reload();
					return;
				case 2:
					System.out.println("[History]\n");
					var images = manager.all();
					if (images.isEmpty()) {
						System.out.println(
							"No images have been submitted yet."
						);
					} else {
						printImageForms(images.toArray(new ImageForm[0]));
					}
					Console.waitForEnter();
					engine.reload();
					return;
				case 0:
					System.out.println("[Exit]\n");
					break;
				default:
					System.out.println("Invalid choice");
					continue;
			}

			break;
		}

		engine.swap(next);
	}

	@Override
	public void exit() {}
}
