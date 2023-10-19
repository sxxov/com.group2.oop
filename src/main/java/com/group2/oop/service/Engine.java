package com.group2.oop.service;

public class Engine {

	private Service service;

	public Service current() {
		return service;
	}

	public Engine(Service initial) {
		this.service = initial;
	}

	public void init() {
		service.init(this);
	}

	public void swap(Service service) {
		this.service.exit();
		this.service = service;
		service.init(this);
	}

	public void reload() {
		service.exit();
		service.init(this);
	}

	public void exit() {
		service.exit();
	}
}
