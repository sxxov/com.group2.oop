package com.group2.oop.console;

public class Console {

	public static void waitForEnter() {
		var c = System.console();
		if (c != null) {
			c.format("\nPress ENTER to proceed.\n");
			c.readLine();
		}
	}
}
