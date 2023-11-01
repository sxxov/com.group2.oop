package com.group2.oop.carbon_credits;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class CarbonCredit {
    private static Map<String, Integer> userCarbonCredit = new HashMap<>();

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            System.out.println("1. Admin: Add Carbon Credit");
            System.out.println("2. User: Check Carbon Credit");
            System.out.println("3. Exit");
            System.out.print("Enter your choice: ");
            int choice = scanner.nextInt();
            switch (choice) {
                case 1:
                    adminAddCarbonCredit(scanner);
                    break;
                case 2:
                    userCheckCarbonCredit(scanner);
                    break;
                case 3:
                    System.out.println("Exiting the program.");
                    scanner.close();
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void adminAddCarbonCredit(Scanner scanner) {
        System.out.print("Enter user ID: ");
        String userId = scanner.next();
        System.out.print("Enter Carbon Credit amount: ");
        int carbonCredit = scanner.nextInt();
        userCarbonCredit.put(userId, userCarbonCredit.getOrDefault(userId, 0) + carbonCredit);
        System.out.println("Carbon Credit added successfully.");
    }

    private static void userCheckCarbonCredit(Scanner scanner) {
        System.out.print("Enter user ID: ");
        String userId = scanner.next();
        if (userCarbonCredit.containsKey(userId)) {
            System.out.println("Carbon Credit balance: " + userCarbonCredit.get(userId));
        } else {
            System.out.println("User not found.");
        }
    }
}