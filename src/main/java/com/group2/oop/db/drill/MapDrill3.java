package com.group2.oop.db.drill;

import com.group2.oop.db.SubRepository;
import javax.annotation.Nullable;

public class MapDrill3<K1, K2, K3, V>
	extends Drillable<K1, SubRepository<K2, SubRepository<K3, V>>> {

	public MapDrill3(
		@Nullable SubRepository<K1, SubRepository<K2, SubRepository<K3, V>>> ref
	) {
		super(ref);
	}

	public MapDrill2<K2, K3, V> drill(K1 k) {
		var v = ref.get(k);
		if (v == null) ref.put(k, v = new SubRepository<>());

		return new MapDrill2<K2, K3, V>(v);
	}
}
