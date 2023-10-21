package com.group2.oop.store;

public interface StoreSubscriber<T> {
	void onValue(T value);
}
