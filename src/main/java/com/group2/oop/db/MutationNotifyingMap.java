package com.group2.oop.db;

import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import javax.annotation.Nullable;

public abstract class MutationNotifyingMap<K, V>
	extends HashMap<K, V>
	implements MutationNotifier {

	@Override
	public V put(K k, V v) {
		var old = super.put(k, v);

		onMutated(k, v, this);

		return old;
	}

	@Override
	public V remove(Object k) {
		var old = super.remove(k);

		if (old != null) onMutated(k, null, this);

		return old;
	}

	@Override
	public void clear() {
		var isAlreadyEmpty = isEmpty();

		super.clear();

		if (!isAlreadyEmpty) onMutated(null, null, this);
	}

	@Override
	public void putAll(@Nullable Map<? extends K, ? extends V> m) {
		super.putAll(m);

		if (m != null) for (var e : m.entrySet()) onMutated(
			e.getKey(),
			e.getValue(),
			this
		);
	}

	@Override
	public V putIfAbsent(K k, V v) {
		var old = super.putIfAbsent(k, v);

		if (old != v) onMutated(k, v, this);

		return old;
	}

	@Override
	public boolean remove(Object k, Object v) {
		var ok = super.remove(k, v);

		if (ok) onMutated(k, null, this);

		return ok;
	}

	@Override
	public boolean replace(K k, V v, V v2) {
		var ok = super.replace(k, v, v2);

		if (ok) onMutated(k, v2, this);

		return ok;
	}

	@Override
	public V replace(K k, V v) {
		var old = super.replace(k, v);

		if (old != v) onMutated(k, v, this);

		return old;
	}

	@Override
	public void replaceAll(
		BiFunction<? super K, ? super V, ? extends V> function
	) {
		super.replaceAll(function);

		for (var e : entrySet()) onMutated(e.getKey(), e.getValue(), this);
	}
}
