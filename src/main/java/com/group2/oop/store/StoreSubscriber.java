package com.group2.oop.store;

public interface StoreSubscriber<T> {
	void onChanged(T value);
}
