package com.group2.oop;

import com.group2.oop.account.AccountManager;
import com.group2.oop.account.AccountService;
import com.group2.oop.account.UserRepository;
import com.group2.oop.carbon_credits.CarbonCreditAccountManager;
import com.group2.oop.carbon_credits.CarbonCreditAccountRepository;
import com.group2.oop.dependency.D;
import com.group2.oop.form.ImageFormManager;
import com.group2.oop.form.ImageFormRepository;
import com.group2.oop.service.Engine;
import com.group2.oop.voucher.VoucherManager;
import com.group2.oop.voucher.VoucherRepository;
import com.group2.oop.rank.RankingSystem;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		System.out.println("Welcome to ${app_name}!");

		D.register(Scanner.class, new Scanner(System.in));
		D.register(UserRepository.class, new UserRepository());
		D.register(AccountManager.class, new AccountManager());
		D.register(ImageFormRepository.class, new ImageFormRepository());
		D.register(ImageFormManager.class, new ImageFormManager());
		D.register(
			CarbonCreditAccountRepository.class,
			new CarbonCreditAccountRepository()
		);
		D.register(
			CarbonCreditAccountManager.class,
			new CarbonCreditAccountManager()
		);
		D.register(VoucherRepository.class, new VoucherRepository());
		D.register(VoucherManager.class, new VoucherManager());
    D.register(RankingSystem.class, new RankingSystem());
		var initialService = new AccountService();
		var engine = new Engine(initialService);
		D.register(Engine.class, engine);
		engine.init();
	}
}
