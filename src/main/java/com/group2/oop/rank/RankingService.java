package com.group2.oop.rank;

import com.group2.oop.carbon_credits.CarbonCreditAccount;
import com.group2.oop.carbon_credits.CarbonCreditAccountRepository;
import com.group2.oop.dependency.D;
import com.group2.oop.home.HomeService;
import com.group2.oop.service.Engine;
import com.group2.oop.service.Service;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

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

	private final CarbonCreditAccount carbonCreditAccount = D.get(
		CarbonCreditAccount.class
	);

	@Override
	public void init(Engine engine) {
		// Fetch the list of user IDs
		List<UUID> userIds = getUsersWithCarbonCredits();

		// Display the ranked users with their received carbon credits
		for (int i = 0; i < userIds.size(); i++) {
			UUID userId = userIds.get(i);
			double carbonCredits = getCarbonCreditsForUser(userId);
			System.out.println(
				(i + 1) +
				". User " +
				userId +
				" - Received Carbon Credits: " +
				carbonCredits
			);
		}

		engine.swap(next);
	}

	@Override
	public void exit() {}

	private List<UUID> getUsersWithCarbonCredits() {
		// Retrieve all user IDs with carbon credits from CarbonCreditAccountRepository
		return new ArrayList<>(carbonCreditAccountRepository.getAllUserIds());
	}

	private double getCarbonCreditsForUser(UUID userId) {
		// Use the CarbonCreditAccountRepository to retrieve the carbon credits for the user
		return carbonCreditAccountRespository.email().balance();
	}
}
