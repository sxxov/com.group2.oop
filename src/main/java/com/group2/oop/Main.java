package com.group2.oop;

import com.group2.oop.account.AccountManager;
import com.group2.oop.account.AccountService;
import com.group2.oop.account.UserRepository;
import com.group2.oop.dependency.D;
import com.group2.oop.service.Engine;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		System.out.println("Hello world!");

		D.register(Scanner.class, new Scanner(System.in));
		D.register(UserRepository.class, new UserRepository());
		D.register(AccountManager.class, new AccountManager());
		var initialService = new AccountService();
		var engine = new Engine(initialService);
		D.register(Engine.class, engine);
		engine.init();
	}
}
