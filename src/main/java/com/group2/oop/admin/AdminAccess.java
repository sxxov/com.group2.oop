package com.group2.oop.admin;

import java.io.*;
import com.group2.oop.home.HomeService;
import com.group2.oop.account.AccountService;
import com.group2.oop.dependency.D;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import com.group2.oop.admin.AdminMenu;
import java.util.Scanner;

public class AdminAccess implements Service {

private final Scanner scanner = D.get(Scanner.class);

  private final Service next;

  public AdminAccess() {
    this.next = new HomeService();
  }

  public AdminAccess(Service next) {
    this.next = next;
  }

  @Override
  public void init(Engine engine) {
    System.out.println("Enter admin password: ");

    for (;;) {
      System.out.print("> ");
      
      String password = scanner.nextLine();

      String verify = "00abc";
      if (password.equals(verify)){
        engine.swap(new AdminMenu());
      } else {
          System.out.println("Invalid password");
      }

      break;
  }

    engine.swap(next);
  }

  @Override
  public void exit() {}

}