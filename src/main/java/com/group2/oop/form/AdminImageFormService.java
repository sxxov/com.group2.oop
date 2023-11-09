package com.group2.oop.form;

import com.group2.oop.account.AccountManager;
import com.group2.oop.account.AccountService;
import com.group2.oop.account.UserRepository;
import com.group2.oop.account.UserRole;
import com.group2.oop.carbon_credits.CarbonCreditAccountManager;
import com.group2.oop.console.Console;
import com.group2.oop.dependency.D;
import com.group2.oop.home.HomeService;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;
import java.util.function.Supplier;

public class AdminImageFormService implements Service {

	private final UserRepository userRepository = D.get(UserRepository.class);
	private final AccountManager account = D.get(AccountManager.class);
	private final Scanner scanner = D.get(Scanner.class);
	private final ImageFormManager imageFormManager = new ImageFormManager();
	private final CarbonCreditAccountManager carbonCreditAccountManager = D.get(
		CarbonCreditAccountManager.class
	);

	private final Service next;

	public AdminImageFormService() {
		this.next = new HomeService();
	}

	public AdminImageFormService(Service next) {
		this.next = next;
	}

	private void printImageForms(ImageForm[] imageForms) {
		System.out.println("---");
		for (int i = 0; i < imageForms.length; i++) {
			var image = imageForms[i];
			var submitter = userRepository.get(image.submitter());

			System.out.println(
				(i + 1) +
				". " +
				(submitter == null ? "<anonymous>" : submitter.email()) +
				"\t : " +
				image.src() +
				"\t - " +
				image.status().get()
			);
		}
		System.out.println("---");
	}

	private void manage(Supplier<ImageForm[]> getPendingImages) {
		var approvedCount = 0;
		var rejectedCount = 0;

		main:for (;;) {
			var pendingImages = getPendingImages.get();

			ImageForm pickImage;
			pick:for (;;) {
				System.out.println("Select an image to manage:");
				printImageForms(pendingImages);
				System.out.println("");
				System.out.println("0. Back");

				int pendingIndex;
				pickIndex:for (;;) {
					try {
						System.out.print("> ");
						pendingIndex = Integer.parseInt(scanner.nextLine());
					} catch (NumberFormatException e) {
						System.out.println("Invalid image number.");
						continue pickIndex;
					}

					if (pendingIndex == 0) break main;

					if (
						pendingIndex < 1 || pendingIndex > pendingImages.length
					) {
						System.out.println(
							"Invalid number. It must be between 1-" +
							pendingImages.length +
							"."
						);
						continue pickIndex;
					}

					break;
				}

				pickImage = pendingImages[pendingIndex - 1];
				System.out.println(
					"[" + pendingIndex + ". " + pickImage.src() + "]\n"
				);
				break pick;
			}

			var submitter = userRepository.get(pickImage.submitter());
			manage:for (;;) {
				System.out.println("Image URL: " + pickImage.src());
				System.out.println(
					"Image submitter: " +
					(submitter == null ? "<anonymous>" : submitter.email())
				);
				System.out.println("Image status: " + pickImage.status().get());
				System.out.println("");
				System.out.println("1. View image");
				System.out.println("2. Approve");
				System.out.println("3. Reject");
				System.out.println("");
				System.out.println("0. Pick another image");

				int manageChoice;
				manageChoice:for (;;) {
					System.out.print("> ");
					try {
						manageChoice = Integer.parseInt(scanner.nextLine());
					} catch (NumberFormatException e) {
						System.out.println("Invalid choice.");
						continue manageChoice;
					}

					switch (manageChoice) {
						case 1:
							System.out.println("[View image]\n");
							try {
								Desktop
									.getDesktop()
									.open(new File(pickImage.src()));
							} catch (IOException e) {
								System.out.println(
									"Failed to open image file."
								);
								e.printStackTrace();
							} catch (IllegalArgumentException e) {
								System.out.println(
									"Image file does not exist."
								);
							}
							continue manage;
						case 2:
							System.out.println("[Approve]\n");
							if (
								pickImage.status().get() ==
								ImageFormStatus.APPROVED
							) {
								System.out.println(
									"Image is already approved."
								);
								continue manage;
							}

							System.out.println(
								"Enter the number of carbon credits to award: "
							);

							double approveReward;
							approveReward:for (;;) {
								try {
									System.out.print("> ");
									approveReward =
										Double.parseDouble(scanner.nextLine());
								} catch (NumberFormatException e) {
									System.out.println(
										"Invalid reward amount."
									);
									continue approveReward;
								}

								break;
							}

							pickImage.approve();
							System.out.println("Image approved.");
							if (submitter != null) {
								var carbonCreditAccount = carbonCreditAccountManager.account(
									submitter.uuid()
								);

								if (carbonCreditAccount == null) {
									System.out.println(
										"Skipped reward because user does not have a carbon credit account."
									);
								} else {
									carbonCreditAccount.deposit(approveReward);
									System.out.println(
										"User was awarded " +
										approveReward +
										" carbon credits."
									);
								}
							}

							System.out.println("");
							++approvedCount;
							continue main;
						case 3:
							System.out.println("[Reject]\n");
							if (
								pickImage.status().get() ==
								ImageFormStatus.REJECTED
							) {
								System.out.println(
									"Image is already rejected."
								);
								continue manage;
							}
							pickImage.reject();
							System.out.println("Image rejected.");
							System.out.println("");
							++rejectedCount;
							continue main;
						case 0:
							break main;
						default:
							System.out.println("Invalid choice.");
							continue manageChoice;
					}
				}
			}
		}

		System.out.println(
			"Approved " +
			approvedCount +
			" images, rejected " +
			rejectedCount +
			" images."
		);
		Console.waitForEnter();
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

		main:for (;;) {
			System.out.println(
				"------------------- Manage images -------------------"
			);
			System.out.println("1. Approve or reject an image");
			System.out.println("2. Manage previous approvals or rejections");
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

				switch (choice) {
					case 1:
						System.out.println("[Approve or reject an image]\n");

						manage(() ->
							imageFormManager
								.allEveryonePending()
								.toArray(new ImageForm[0])
						);

						continue main;
					case 2:
						System.out.println(
							"[Manage previous approvals or rejections]\n"
						);
						history:for (;;) {
							System.out.println("1. Manage approved images");
							System.out.println("2. Manage rejected images");
							System.out.println(
								"3. Manage approved or rejected images"
							);
							System.out.println("");
							System.out.println("0. Back");

							int historyFilterChoice;
							Supplier<ImageForm[]> getHistoryImages;
							historyFilterChoice:for (;;) {
								System.out.print("> ");
								try {
									historyFilterChoice =
										Integer.parseInt(scanner.nextLine());
								} catch (NumberFormatException e) {
									System.out.println("Invalid choice.");
									continue historyFilterChoice;
								}

								switch (historyFilterChoice) {
									case 1:
										System.out.println(
											"[Manage approved images]\n"
										);
										getHistoryImages =
											() ->
												imageFormManager
													.allEveryoneApproved()
													.toArray(new ImageForm[0]);
										break historyFilterChoice;
									case 2:
										System.out.println(
											"[Manage rejected images]\n"
										);
										getHistoryImages =
											() ->
												imageFormManager
													.allEveryoneRejected()
													.toArray(new ImageForm[0]);
										break historyFilterChoice;
									case 3:
										System.out.println(
											"[Manage approved or rejected images]\n"
										);
										getHistoryImages =
											() ->
												imageFormManager
													.allEveryone()
													.toArray(new ImageForm[0]);
										break historyFilterChoice;
									case 0:
										System.out.println("[Back]\n");
										break main;
									default:
										System.out.println("Invalid choice.");
										continue historyFilterChoice;
								}
							}

							manage(getHistoryImages);
							continue history;
						}
					case 0:
						System.out.println("[Back]\n");
						break main;
					default:
						System.out.println("Invalid choice.");
						continue mainChoice;
				}
			}
		}

		engine.swap(next);
	}

	@Override
	public void exit() {}
}
