package com.group2.oop.voucher;

import com.group2.oop.admin.AdminHomeService;
import com.group2.oop.dependency.D;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import java.util.Scanner;


public class adminVoucher implements Service {
  private final Scanner scanner = D.get(Scanner.class);

  private final VoucherAdminRepository repository = D.get(
		VoucherAdminRepository.class
	);

  public void addVoucher(VoucherAdminRepository voucher) {
      repository.add(voucher); // Save the updated vouchers
  }

  public void removeVoucher(String code) {
      repository.remove(code); // Save the updated vouchers
  }

  @Override
  public void init(Engine engine) {
    int choice;

    while (true) {
      System.out.println("------------------- Edit voucher -------------------");
      System.out.println("1. View Vouchers");
      System.out.println("2. Add a voucher");
      System.out.println("3. Remove a voucher");
      System.out.println("4. Exit");
      System.out.print("> ");

      try {
        choice = Integer.parseInt(scanner.nextLine());
      } catch (NumberFormatException e) {
        System.out.println("Invalid choice.");
        continue;
      }

      switch (choice) {
        case 1:
          //Option 1: Display list of vouchers
          System.out.println("List of Vouchers:");
          for (VoucherAdminRepository voucher : repository.all()) {
              System.out.println("Code: " + voucher.getCode() + ", Description: " + voucher.getDescription() + ", Credits Required: " + voucher.getCreditsRequired());
          }
          break;

        case 2:
          // Option 2: Add Voucher
          System.out.print("Enter voucher code: ");
          String code = scanner.nextLine();
          System.out.print("Enter voucher description: ");
          String description = scanner.nextLine();
          System.out.print("Enter credits required: ");
          int creditsRequired = scanner.nextInt();
          scanner.nextLine();
          addVoucher(new VoucherAdminRepository(code, description, creditsRequired));
          System.out.println("Voucher added successfully.");
          break;

        case 3:
          // Option 3: Remove Voucher
          System.out.print("Enter voucher code to remove: ");
          String code2 = scanner.nextLine();
          removeVoucher(code2);
          System.out.println("Voucher removed successfully.");
          break;

        case 4:
          //Option 4: go back to the admin menu
          engine.swap(new AdminHomeService());
          return;

        default:
          System.out.println("Invalid option. Please choose again.");
      }
    }
  }

  @Override
  public void exit() {
    repository.write(); // Save the vouchers when exiting the program
  }
}
