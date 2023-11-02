package com.group2.oop.db;

import com.group2.oop.store.ReadableStore;
import java.lang.reflect.InaccessibleObjectException;

public abstract class EffectSubscribingMap<K, V>
	extends MutationNotifyingMap<K, V> {

	protected void subscribeToEffects() {
		for (var entry : entrySet()) {
			var k = entry.getKey();
			var v = entry.getValue();
			var clazz = v.getClass();
			var fields = clazz.getDeclaredFields();

			for (var field : fields) {
				if (field.isAnnotationPresent(Effect.class)) {
					try {
						field.setAccessible(true);
						var value = field.get(v);
						if (value instanceof ReadableStore<?> store) {
							store.subscribeLazy(vv -> {
								onMutated(k, v, this);
							});
						}
					} catch (IllegalAccessException e) {
						System.out.println(
							"Error subscribing to write on change store."
						);
						e.printStackTrace();
					} catch (InaccessibleObjectException e) {
						System.out.println(
							"Attempted to declare an inaccessible field(" +
							field.getName() +
							") as an effect."
						);
						e.printStackTrace();
					}
				}
			}
		}
	}
}
