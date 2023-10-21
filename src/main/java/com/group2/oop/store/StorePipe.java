package com.group2.oop.store;

public interface StorePipe<T, R> {
	R pipe(T value);
}
