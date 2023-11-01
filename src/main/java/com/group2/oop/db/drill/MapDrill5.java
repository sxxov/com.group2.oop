package com.group2.oop.db.drill;

import com.group2.oop.db.SubRepository;
import javax.annotation.Nullable;

public class MapDrill5<K1, K2, K3, K4, K5, V>
	extends Drillable<K1, SubRepository<K2, SubRepository<K3, SubRepository<K4, SubRepository<K5, V>>>>> {

	public MapDrill5(
		@Nullable SubRepository<K1, SubRepository<K2, SubRepository<K3, SubRepository<K4, SubRepository<K5, V>>>>> ref
	) {
		super(ref);
	}

	public MapDrill4<K2, K3, K4, K5, V> drill(K1 k) {
		var v = ref.get(k);
		if (v == null) ref.put(k, v = new SubRepository<>());

		return new MapDrill4<K2, K3, K4, K5, V>(v);
	}
}
