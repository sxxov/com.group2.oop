package com.group2.oop.db;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.function.BiFunction;
import javax.annotation.Nullable;

public abstract class MutationNotifyingMap<K, V>
	extends HashMap<K, V>
	implements MutationNotifier {

	private static final long serialVersionUID = 1L;

	private transient ArrayList<MutationNotifier> mutationNotifiers = new ArrayList<>();

	private void readObject(java.io.ObjectInputStream in)
		throws java.io.IOException, ClassNotFoundException {
		in.defaultReadObject();
		mutationNotifiers = new ArrayList<>();
	}

	public void registerOnMutated(MutationNotifier notifier) {
		mutationNotifiers.add(notifier);
	}

	protected void trigger(Object k, Object v) {
		for (var notifier : mutationNotifiers) notifier.onMutated(k, v, this);
		onMutated(k, v, this);
	}

	@Override
	public V put(K k, V v) {
		var old = super.put(k, v);

		trigger(k, v);

		return old;
	}

	@Override
	public V remove(Object k) {
		var old = super.remove(k);

		if (old != null) trigger(k, null);

		return old;
	}

	@Override
	public void clear() {
		var isAlreadyEmpty = isEmpty();

		super.clear();

		if (!isAlreadyEmpty) trigger(null, null);
	}

	@Override
	public void putAll(@Nullable Map<? extends K, ? extends V> m) {
		super.putAll(m);

		if (m != null) for (var e : m.entrySet()) trigger(
			e.getKey(),
			e.getValue()
		);
	}

	@Override
	public V putIfAbsent(K k, V v) {
		var old = super.putIfAbsent(k, v);

		if (old != v) trigger(k, v);

		return old;
	}

	@Override
	public boolean remove(Object k, Object v) {
		var ok = super.remove(k, v);

		if (ok) trigger(k, null);

		return ok;
	}

	@Override
	public boolean replace(K k, V v, V v2) {
		var ok = super.replace(k, v, v2);

		if (ok) trigger(k, v2);

		return ok;
	}

	@Override
	public V replace(K k, V v) {
		var old = super.replace(k, v);

		if (old != v) trigger(k, v);

		return old;
	}

	@Override
	public void replaceAll(
		BiFunction<? super K, ? super V, ? extends V> function
	) {
		super.replaceAll(function);

		for (var e : entrySet()) trigger(e.getKey(), e.getValue());
	}
}
