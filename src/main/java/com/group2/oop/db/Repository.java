package com.group2.oop.db;

import com.group2.oop.serialisation.MapIO;
import java.io.IOException;
import java.io.ObjectInputStream;
import javax.annotation.Nullable;

public class Repository<K, V> extends SubRepository<K, V> {

	private final String id;
	private transient MapIO<K, V> io;

	private void readObject(ObjectInputStream in)
		throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		io = new MapIO<>(this, getPath(id));
	}

	public Repository<K, V> ref() {
		return this;
	}

	public Repository() {
		this("");
	}

	public Repository(String id) {
		this.id = id;

		io = new MapIO<>(this, getPath(id));

		try {
			putAll(io.read());
		} catch (IOException e) {
			System.out.println(
				"Error reading repository(" + id + ") from disk."
			);
			e.printStackTrace();
		}

		subscribeToEffects();
	}

	protected String getPath(String id) {
		return (
			".data/repositories/" +
			(id.length() <= 0 ? getClass().getSimpleName() : id) +
			".ser"
		);
	}

	public String id() {
		return id;
	}

	@Override
	public void onMutate(
		@Nullable Object k,
		@Nullable Object v,
		MutationNotifier ctx
	) {
		write();
	}

	private void write() {
		try {
			io.write();
		} catch (IOException e) {
			System.out.println("Error writing repository(" + id + ") to disk.");
			e.printStackTrace();
		}
	}
}
