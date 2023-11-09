package com.group2.oop.rank;

import com.group2.oop.account.AccountManager;
import com.group2.oop.account.User;
import com.group2.oop.account.UserRepository;
import com.group2.oop.carbon_credits.CarbonCreditAccountRepository;
import com.group2.oop.console.Console;
import com.group2.oop.dependency.D;
import com.group2.oop.home.HomeService;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import java.util.AbstractMap.SimpleEntry;
import java.util.List;
import java.util.Scanner;

public class RankingService implements Service {

	private final Service next;

	public RankingService() {
		this.next = new HomeService();
	}

	public RankingService(Service next) {
		this.next = next;
	}

	private final CarbonCreditAccountRepository carbonCreditAccountRepository = D.get(
		CarbonCreditAccountRepository.class
	);

	private final UserRepository userRepository = D.get(UserRepository.class);
	private final AccountManager account = D.get(AccountManager.class);
	private final Scanner scanner = D.get(Scanner.class);

	@Override
	public void init(Engine engine) {
		main:for (;;) {
			System.out.println(
				"------------------- Your ranking -------------------"
			);
			System.out.println("Global: #" + getCurrentUserRank());
			System.out.println("");
			System.out.println("1. View global leaderboard");
			System.out.println("");
			System.out.println("0. Exit");

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
						System.out.println("[View global leaderboard]\n");
						printLeaderboard();

						Console.waitForEnter();
						continue main;
					case 0:
						System.out.println("[Exit]\n");
						break main;
				}
			}
		}

		engine.swap(next);
	}

	@Override
	public void exit() {}

	private int getCurrentUserRank() {
		var userToAmount = getAllUserAndAmount();

		var i = 0;
		for (var entry : userToAmount) {
			var user = entry.getKey();
			if (user.uuid().equals(account.current().get().uuid())) {
				return i;
			}
			++i;
		}

		return -1;
	}

	private List<SimpleEntry<User, Double>> getAllUserAndAmount() {
		return carbonCreditAccountRepository
			.entrySet()
			.stream()
			.map(entry -> {
				// get user & carbon credit amount first to be thread safe
				var uuid = entry.getKey();
				var user = userRepository.get(uuid);
				var credit = entry.getValue();
				var amount = credit.amount().get();

				return new SimpleEntry<User, Double>(user, amount);
			})
			.sorted((a, b) -> a.getValue().compareTo(b.getValue()))
			.toList();
	}

	private void printLeaderboard() {
		var userToAmount = getAllUserAndAmount();

		var i = 0;
		for (var entry : userToAmount) {
			var user = entry.getKey();
			var amount = entry.getValue();
			System.out.println((i + 1) + ". " + user.email() + "\t- " + amount);
			++i;
		}
	}
}
