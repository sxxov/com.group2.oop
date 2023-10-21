package com.group2.oop.store;

public interface ReadableStore<T> {
	StoreSubscribed<T> subscribe(StoreSubscriber<T> subscriber);
	StoreSubscribed<T> subscribeLazy(StoreSubscriber<T> subscriber);
	void unsubscribe(StoreSubscriber<T> subscriber);
	T get();
	void trigger();
	<R> ReadableStore<R> derive(StorePipe<T, R> updater);
}
