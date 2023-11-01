package com.group2.oop.voucher;

import com.group2.oop.db.MutationNotifier;
import com.group2.oop.db.Repository;
import com.group2.oop.db.SubRepository;
import com.group2.oop.db.drill.MapDrill1;
import com.group2.oop.db.drill.MapDrill2;
import java.util.UUID;
import javax.annotation.Nullable;

public class VoucherRepository
	extends Repository<UUID, SubRepository<String, Voucher>> {

	public MapDrill1<String, Voucher> drill(UUID k) {
		return new MapDrill2<>(this).drill(k);
	}

	@SuppressWarnings("unchecked")
	@Override
	public void onMutate(
		@Nullable Object k,
		@Nullable Object v,
		MutationNotifier ctx
	) {
		if (v instanceof SubRepository subRepo && k instanceof UUID uuid) {
			for (var e : subRepo.entrySet()) {
				var voucher = ((Entry<String, Voucher>) e).getValue();

				if (voucher.claimant().get() != uuid) {
					voucher.claim(uuid);
				}
			}
		}

		super.onMutate(k, v, ctx);
	}
}
