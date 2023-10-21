package com.group2.oop.form;

import com.group2.oop.serialisation.MapIO;
import com.group2.oop.store.StoreSubscriber;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class ImageFormRepository implements Serializable {

	private static final String FORM_IMAGE_REPOSITORY_PATH = "images.ser";

	private HashMap<String, ImageForm> images = new HashMap<>();
	private MapIO<String, ImageForm> io = new MapIO<>(
		images,
		FORM_IMAGE_REPOSITORY_PATH
	);

	private final StoreSubscriber<ImageFormStatus> onAnyImageStatusChange = status -> {
		write();
	};

	public ImageFormRepository() {
		try {
			images = io.read();
			for (var image : images.values()) {
				image.status().subscribeLazy(onAnyImageStatusChange);
			}
		} catch (IOException e) {
			System.out.println("Error reading images from disk.");
			e.printStackTrace();
		}
	}

	public void add(ImageForm image) {
		images.put(image.src(), image);
		image.status().subscribeLazy(onAnyImageStatusChange);
		write();
	}

	public Optional<ImageForm> get(String src) {
		return Optional.ofNullable(images.get(src));
	}

	public Collection<ImageForm> all() {
		return images.values();
	}

	public void remove(String src) {
		var image = images.get(src);
		if (image == null) return;
		remove(image);
	}

	public void remove(ImageForm image) {
		image.status().unsubscribe(onAnyImageStatusChange);
		images.remove(image.src());
		write();
	}

	private void write() {
		try {
			io.write();
		} catch (IOException e) {
			System.out.println("Error writing images.");
			e.printStackTrace();
		}
	}
}
