package com.group2.oop.voucher;

import com.group2.oop.account.AccountManager;
import com.group2.oop.account.AccountService;
import com.group2.oop.home.HomeService;
import com.group2.oop.dependency.D;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import java.util.Scanner;
import java.util.ArrayList;
import java.util.List;

public class userVoucher implements Service {
    public final AccountManager account = D.get(AccountManager.class);
    public final Scanner scanner = D.get(Scanner.class);

    private final VoucherAdminRepository repository = D.get(
    VoucherAdminRepository.class
  );

    private final VoucherUserRepository repositoryUser = D.get(
    VoucherUserRepository.class
  );

    public void addVoucher(VoucherUserRepository voucherUser) {
      repositoryUser.add(voucherUser); // Save the updated vouchers
  }

    private int credits = 100; // Starting carbon credits


    @Override
    public void init(Engine engine) {
        if (account.current().isEmpty()) {
            engine.swap(new AccountService());
            return;
        }

        while (true) {

          System.out.println("------------------- Available Voucher-------------------");
          System.out.println("1. View voucher listings");
          System.out.println("2. Redeem voucher");
          System.out.println("3. View your vouchers");
          System.out.println("4. Exit");

            int choice;
            try {
                choice = Integer.parseInt(scanner.nextLine());
            } catch (NumberFormatException e) {
                System.out.println("Invalid choice.");
                continue;
            }

            switch (choice) {

                case 1:
                    // View voucher listings
                    System.out.println("List of Vouchers:");
                    for (VoucherAdminRepository voucher : repository.all()) {
                        System.out.println("Code: " + voucher.getCode() + ", Description: " + voucher.getDescription() + ", Credits Required: " + voucher.getCreditsRequired());
                    }
                    break;
                case 2:
                    // Redeem voucher
                    redeemVoucher();
                    break;
                case 3:
                    System.out.println("List of Your redeemed vouchers:");
                    for (VoucherUserRepository voucherUser : repositoryUser.all()) {
                        System.out.println("Code: " + voucherUser.getCode() + ", Description: " + voucherUser.getDescription());
                    }
                    break;
                case 4:
                    engine.swap(new HomeService());
                    return;
                default:
                    System.out.println("Invalid choice.");
            }
        }
    }

    private void redeemVoucher() {
        System.out.print("Enter voucher code to redeem: ");
        String code = scanner.nextLine();

        for (VoucherAdminRepository voucher : repository.all()) {
            if (voucher.getCode().equals(code)) {
                int creditsRequired = voucher.getCreditsRequired();
                if (creditsRequired > 0) {
                    if (credits >= creditsRequired) {
                        System.out.println("Voucher redeemed successfully.");
                        System.out.println("Deducted " + creditsRequired + " credits.");

                        deductCredits(creditsRequired); // Deduct the required credits
                        addVoucher(new VoucherUserRepository(voucher.getCode(), voucher.getDescription()));

                        // Print the user's total credits left
                        System.out.println("Total credits left: " + getCredits());

                      break;

                    } else {
                        System.out.println("Insufficient credits to redeem this voucher.");
                    }
                } else {
                    System.out.println("This voucher cannot be redeemed.");
                }
                return;
            } else {
                System.out.println("Voucher not found.");
            }
        }
    }

    // Method to deduct credits
    private void deductCredits(int creditsToDeduct) {
        credits -= creditsToDeduct;
    }

    // Method to get user's current credits
    public int getCredits() {
        return credits;
    }

    @Override
    public void exit() {
    }
}