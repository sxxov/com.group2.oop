package com.group2.oop.dependency;

import java.util.HashMap;

public class D {

	private static final HashMap<Class<?>, Object> dependencyMap = new HashMap<>();

	public static <T> T get(Class<T> clazz) {
		var instance = dependencyMap.get(clazz);

		if (instance == null) {
			throw new RuntimeException(
				"Dependency not found for class: " + clazz.getName()
			);
		}

		return clazz.cast(instance);
	}

	public static <T> void register(Class<T> clazz, T instance) {
		dependencyMap.put(clazz, instance);
	}
}
