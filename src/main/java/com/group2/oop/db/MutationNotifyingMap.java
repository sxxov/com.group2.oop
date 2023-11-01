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

		onMutate(k, v, this);

		return old;
	}

	@Override
	public V remove(Object k) {
		var old = super.remove(k);

		if (old != null) onMutate(k, null, this);

		return old;
	}

	@Override
	public void clear() {
		var isAlreadyEmpty = isEmpty();

		super.clear();

		if (!isAlreadyEmpty) onMutate(null, null, this);
	}

	@Override
	public void putAll(@Nullable Map<? extends K, ? extends V> m) {
		super.putAll(m);

		if (m != null) for (var e : m.entrySet()) onMutate(
			e.getKey(),
			e.getValue(),
			this
		);
	}

	@Override
	public V putIfAbsent(K k, V v) {
		var old = super.putIfAbsent(k, v);

		if (old != v) onMutate(k, v, this);

		return old;
	}

	@Override
	public boolean remove(Object k, Object v) {
		var ok = super.remove(k, v);

		if (ok) onMutate(k, null, this);

		return ok;
	}

	@Override
	public boolean replace(K k, V v, V v2) {
		var ok = super.replace(k, v, v2);

		if (ok) onMutate(k, v2, this);

		return ok;
	}

	@Override
	public V replace(K k, V v) {
		var old = super.replace(k, v);

		if (old != v) onMutate(k, v, this);

		return old;
	}

	@Override
	public void replaceAll(
		BiFunction<? super K, ? super V, ? extends V> function
	) {
		super.replaceAll(function);

		for (var e : entrySet()) onMutate(e.getKey(), e.getValue(), this);
	}
}
