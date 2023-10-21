package com.group2.oop.store;

public interface WritableStore<T> extends ReadableStore<T> {
	void set(T value);
	void update(StorePipe<T, T> updater);
	<R> WritableStore<R> derive(StorePipe<T, R> updater);
}
