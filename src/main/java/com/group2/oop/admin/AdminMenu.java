package com.group2.oop.admin;

import java.io.IOException;
import com.group2.oop.service.Service;
import com.group2.oop.account.AccountService;
import com.group2.oop.home.HomeService;
import com.group2.oop.dependency.D;
import com.group2.oop.service.Engine;
import com.group2.oop.form.storeImage;
import java.util.Scanner;

public class AdminMenu implements Service {

    private final Scanner scanner = D.get(Scanner.class);

    private final Service next;

    public AdminMenu() {
        this.next = new HomeService();
    }

    public AdminMenu(Service next) {
        this.next = next;
    }

    storeImage store = new storeImage();

    @Override
    public void init(Engine engine) {

        int choice;
        while (true) {
            System.out.println("------------------- Admin Menu -------------------");
            System.out.println("1. Approve an image");
            System.out.println("2. Show all submitted images");
            System.out.println("3. Show all approved images");
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
                    System.out.println("------------------- Approve an image -------------------");
                    try {
                        store.showSubmit();
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Error storing the image.");
                        e.printStackTrace();
                    }
                    System.out.println("Enter the number of the image to approve: ");
                    int input = Integer.parseInt(scanner.nextLine());

                    try {
                        store.storeApproved(input);
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Error storing the image.");
                        e.printStackTrace();
                    }
                    break;

                case 2:
                    System.out.println("------------------- Submitted images -------------------");
                    try {
                        store.showSubmit();
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Error storing the image.");
                        e.printStackTrace();
                    }
                    break;

                case 3:
                    System.out.println("------------------- Approved images -------------------");
                    try {
                        store.showApproved();
                    } catch (IOException | ClassNotFoundException e) {
                        System.out.println("Error storing the image.");
                        e.printStackTrace();
                    }
                    break;

                case 0:
                     engine.swap(new AccountService());
                    return;

                default:
                    System.out.println("Invalid choice");
            }
        }
    }

    @Override
    public void exit() {}

}
