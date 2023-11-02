package com.group2.oop.db;

import com.group2.oop.store.Store;
import javax.annotation.Nullable;

public class SubRepository<K, V> extends EffectSubscribingMap<K, V> {

	@Effect
	private Store<Integer> store = new Store<>(0);

	public SubRepository() {
		subscribeToEffects();
	}

	@Override
	public void onMutated(
		@Nullable Object k,
		@Nullable Object v,
		MutationNotifier ctx
	) {
		store.trigger();
	}
}
