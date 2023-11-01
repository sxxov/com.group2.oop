package com.group2.oop.db.drill;

import com.group2.oop.db.SubRepository;
import javax.annotation.Nullable;

public class MapDrill4<K1, K2, K3, K4, V>
	extends Drillable<K1, SubRepository<K2, SubRepository<K3, SubRepository<K4, V>>>> {

	public MapDrill4(
		@Nullable SubRepository<K1, SubRepository<K2, SubRepository<K3, SubRepository<K4, V>>>> ref
	) {
		super(ref);
	}

	public MapDrill3<K2, K3, K4, V> drill(K1 k) {
		var v = ref.get(k);
		if (v == null) ref.put(k, v = new SubRepository<>());

		return new MapDrill3<K2, K3, K4, V>(v);
	}
}
