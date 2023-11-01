package com.group2.oop.db.drill;

import com.group2.oop.db.SubRepository;
import javax.annotation.Nullable;

public class MapDrill1<K, V> extends Drillable<K, V> {

	public MapDrill1(@Nullable SubRepository<K, V> ref) {
		super(ref);
	}

	public MapDrill0<K, V> drill(K k) {
		return new MapDrill0<K, V>(ref, k, ref.get(k));
	}
}
