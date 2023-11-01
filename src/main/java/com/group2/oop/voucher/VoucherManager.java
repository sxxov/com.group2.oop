package com.group2.oop.voucher;

import com.group2.oop.dependency.D;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

public class VoucherManager {

	private final VoucherRepository repository = D.get(VoucherRepository.class);

	public void remove(UUID uuid, Voucher voucher) {
		repository.drill(uuid).drill(voucher.id()).remove();
		voucher.unclaim();
	}

	public void add(UUID uuid, Voucher voucher) {
		repository.drill(uuid).drill(voucher.id()).put(voucher);
	}

	public void removeNoClaimant(Voucher voucher) {
		repository.drill(Voucher.NO_CLAIMANT).drill(voucher.id()).remove();
	}

	public void addNoClaimant(Voucher voucher) {
		repository.drill(Voucher.NO_CLAIMANT).drill(voucher.id()).put(voucher);
	}

	public Collection<Voucher> allEveryone() {
		return repository
			.values()
			.stream()
			.flatMap(subRepo -> subRepo.values().stream())
			.collect(Collectors.toList());
	}

	public Collection<Voucher> allEveryoneUnclaimed() {
		return repository
			.values()
			.stream()
			.flatMap(subRepo -> subRepo.values().stream())
			.filter(voucher -> !voucher.claimed())
			.collect(Collectors.toList());
	}

	public Collection<Voucher> allEveryoneClaimed() {
		return repository
			.values()
			.stream()
			.flatMap(subRepo -> subRepo.values().stream())
			.filter(voucher -> voucher.claimed())
			.collect(Collectors.toList());
	}
}
