package com.group2.oop.store;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.Serializable;
import java.util.ArrayList;
import javax.annotation.Nullable;

public class Store<T> implements WritableStore<T>, Serializable {

	private static final long serialVersionUID = 1L;

	private T value;
	private transient ArrayList<StoreSubscriber<T>> subscribers = new ArrayList<>();

	public Store(T initial) {
		this.value = initial;
	}

	@Nullable
	private transient Supply<T> supply = null;

	public Supply<T> supply() {
		if (supply == null) {
			supply = new Supply<T>(this);
		}

		return supply;
	}

	public T get() {
		return value;
	}

	public void set(T value) {
		this.value = value;
		trigger(value);
	}

	public void update(StorePipe<T, T> pipe) {
		set(pipe.pipe(value));
	}

	public void trigger() {
		trigger(value);
	}

	private void trigger(T value) {
		for (var subscriber : subscribers) {
			subscriber.onChanged(value);
		}
	}

	public StoreSubscribed<T> subscribe(StoreSubscriber<T> subscriber) {
		subscribers.add(subscriber);
		subscriber.onChanged(value);

		return () -> {
			unsubscribe(subscriber);
		};
	}

	public StoreSubscribed<T> subscribeLazy(StoreSubscriber<T> subscriber) {
		subscribers.add(subscriber);

		return () -> {
			unsubscribe(subscriber);
		};
	}

	public void unsubscribe(StoreSubscriber<T> subscriber) {
		subscribers.remove(subscriber);
	}

	public <R> Store<R> derive(StorePipe<T, R> pipe) {
		var store = new Store<R>(pipe.pipe(value));

		subscribeLazy(value -> {
			store.set(pipe.pipe(value));
		});

		return store;
	}

	private void readObject(ObjectInputStream in)
		throws IOException, ClassNotFoundException {
		in.defaultReadObject();
		subscribers = new ArrayList<>();
	}
}
