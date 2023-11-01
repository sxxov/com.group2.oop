package com.group2.oop.db.drill;

import com.group2.oop.db.SubRepository;
import javax.annotation.Nullable;

public class MapDrill2<K1, K2, V> extends Drillable<K1, SubRepository<K2, V>> {

	public MapDrill2(@Nullable SubRepository<K1, SubRepository<K2, V>> ref) {
		super(ref);
	}

	public MapDrill1<K2, V> drill(K1 k) {
		var v = ref.get(k);
		if (v == null) ref.put(k, v = new SubRepository<>());

		return new MapDrill1<K2, V>(v);
	}
}
