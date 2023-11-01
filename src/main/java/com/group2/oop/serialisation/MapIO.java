package com.group2.oop.serialisation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.ParameterizedType;
import java.util.Arrays;
import java.util.HashMap;

public class MapIO<K, V> implements IO<HashMap<K, V>> {

	private final String path;
	private final HashMap<K, V> ref;

	public HashMap<K, V> ref() {
		return ref;
	}

	public MapIO(HashMap<K, V> ref, String path) {
		this.ref = ref;
		this.path = path;
	}

	@SuppressWarnings("unchecked")
	public HashMap<K, V> read() throws IOException {
		try (
			var fileIn = new FileInputStream(path);
			var in = new ObjectInputStream(fileIn)
		) {
			if (
				!(in.readObject() instanceof HashMap<?, ?> readMap)
			) throw new RuntimeException(
				"Fatally invalid data type in " +
				path +
				". Expected HashMap<?, ?>."
			);

			for (var entry : readMap.entrySet()) {
				var kRaw = entry.getKey();
				var vRaw = entry.getValue();
				K k;
				V v;

				try {
					k = (K) kRaw;
					v = (V) vRaw;
				} catch (ClassCastException e) {
					var typeArgs =
						(
							(
								(ParameterizedType) ref
									.getClass()
									.getGenericSuperclass()
							).getActualTypeArguments()
						);
					var typeArgNames = Arrays
						.stream(typeArgs, 0, typeArgs.length)
						.map(typeArg -> typeArg.getTypeName())
						.toArray(String[]::new);
					System.out.println(
						"Invalid data type in row of " +
						path +
						". Expected " +
						String.join(", ", typeArgNames) +
						". Found " +
						kRaw.getClass().getName() +
						", " +
						vRaw.getClass().getName() +
						"."
					);

					continue;
				}

				ref.put(k, v);
			}

			return ref;
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Could not find HashMap class??");
		} catch (FileNotFoundException e) {
			write();

			return ref;
		}
	}

	public void write() throws IOException {
		try (
			var fileOut = new java.io.FileOutputStream(path);
			var out = new java.io.ObjectOutputStream(fileOut)
		) {
			out.writeObject(this.ref);
		}
	}
}
