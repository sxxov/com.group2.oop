package com.group2.oop;

import com.group2.oop.account.AccountManager;
import com.group2.oop.account.AccountService;
import com.group2.oop.account.UserRepository;
import com.group2.oop.dependency.D;
import com.group2.oop.form.ImageFormManager;
import com.group2.oop.form.ImageFormRepository;
import com.group2.oop.voucher.VoucherAdminRepository;
import com.group2.oop.voucher.VoucherUserRepository;
import com.group2.oop.service.Engine;

import java.util.Scanner;

public class Main {

  public static void main(String[] args) {
    System.out.println("Welcome to ${app_name}!");

    D.register(Scanner.class, new Scanner(System.in));
    D.register(UserRepository.class, new UserRepository());
    D.register(AccountManager.class, new AccountManager());
    D.register(ImageFormRepository.class, new ImageFormRepository());
    D.register(ImageFormManager.class, new ImageFormManager());
    D.register(VoucherAdminRepository.class, new VoucherAdminRepository());
    D.register(VoucherUserRepository.class, new VoucherUserRepository());
    var initialService = new AccountService();
    var engine = new Engine(initialService);
    D.register(Engine.class, engine);
    engine.init();
  }
}
