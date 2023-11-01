package com.group2.oop.serialisation;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.lang.reflect.ParameterizedType;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;

public class ListIO<T> implements IO<ArrayList<T>> {

	private final String path;
	private final ArrayList<T> ref;

	public ArrayList<T> ref() {
		return ref;
	}

	public ListIO(Collection<T> ref, String path) {
		this.ref = new ArrayList<>(ref);
		this.path = path;
	}

	@SuppressWarnings("unchecked")
	public ArrayList<T> read() throws IOException {
		try (
			var fileIn = new FileInputStream(path);
			var in = new ObjectInputStream(fileIn)
		) {
			if (
				!(in.readObject() instanceof ArrayList<?> readList)
			) throw new RuntimeException(
				"Fatally invalid data type in " +
				path +
				". Expected ArrayList<?>."
			);

			for (var vRaw : readList) {
				T v;

				try {
					v = (T) vRaw;
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
						vRaw.getClass().getName() +
						"."
					);

					continue;
				}

				ref.add(v);
			}

			return ref;
		} catch (ClassNotFoundException e) {
			throw new IllegalStateException("Could not find ArrayList class??");
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
