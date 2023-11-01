package com.group2.oop.db.drill;

import com.group2.oop.db.SubRepository;
import java.io.Serializable;
import javax.annotation.Nullable;

public class Drillable<K, V> implements Serializable {

	protected SubRepository<K, V> ref;

	public SubRepository<K, V> ref() {
		return ref;
	}

	public Drillable() {
		this(null);
	}

	public Drillable(@Nullable SubRepository<K, V> ref) {
		this.ref = ref == null ? new SubRepository<>() : ref;
	}
}
