package com.group2.oop.db.drill;

import com.group2.oop.db.SubRepository;
import java.util.function.Function;
import javax.annotation.Nullable;

public class MapDrill0<K, V> {

	private SubRepository<K, V> parent;
	private K k;
	private V v;

	public MapDrill0(SubRepository<K, V> parent, K k, V v) {
		this.parent = parent;
		this.k = k;
		this.v = v;
	}

	public @Nullable V drill() {
		return v;
	}

	public boolean isEmpty() {
		return v == null;
	}

	public boolean isPresent() {
		return v != null;
	}

	public V get() throws NullPointerException {
		if (v == null) throw new NullPointerException();

		return v;
	}

	public V orElse(Function<? super K, ? extends V> other) {
		if (v == null) return other.apply(k);

		return v;
	}

	public V orElsePut(Function<? super K, ? extends V> other) {
		if (v == null) put(other.apply(k));

		return v;
	}

	public void put(@Nullable V v) {
		this.v = v;
		parent.put(k, v);
	}

	public void remove() {
		parent.remove(k);
	}
}
