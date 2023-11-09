package com.group2.oop.db;

import com.group2.oop.store.Store;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.annotation.Nullable;

public class SubRepository<K, V> extends EffectSubscribingMap<K, V> {

	private static final long serialVersionUID = 1L;

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

	private void readObject(ObjectInputStream in)
		throws IOException, ClassNotFoundException {
		in.defaultReadObject();

		subscribeToEffects();
	}
}
