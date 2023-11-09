package com.group2.oop.store;

import java.io.Serializable;

public class Supply<T> implements ReadableStore<T>, Serializable {

	private static final long serialVersionUID = 1L;

	private Store<T> store;

	public Supply(Store<T> store) {
		this.store = store;
	}

	public T get() {
		return store.get();
	}

	public void trigger() {
		store.trigger();
	}

	public StoreSubscribed<T> subscribe(StoreSubscriber<T> subscriber) {
		return store.subscribe(subscriber);
	}

	public StoreSubscribed<T> subscribeLazy(StoreSubscriber<T> subscriber) {
		return store.subscribeLazy(subscriber);
	}

	public void unsubscribe(StoreSubscriber<T> subscriber) {
		store.unsubscribe(subscriber);
	}

	public <R> ReadableStore<R> derive(StorePipe<T, R> updater) {
		return store.derive(updater).supply();
	}
}
