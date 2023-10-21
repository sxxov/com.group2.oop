package com.group2.oop.serialisation;

import java.io.IOException;

public interface IO<T> {
	T read() throws IOException;
	void write() throws IOException;
}
