package com.group2.oop.form;

import com.group2.oop.account.AccountManager;
import com.group2.oop.account.UnauthorisedException;
import com.group2.oop.dependency.D;
import java.util.Collection;

public class ImageFormManager {

	private final ImageFormRepository repository = D.get(
		ImageFormRepository.class
	);

	private final AccountManager account = D.get(AccountManager.class);

	public ImageForm submit(String src) throws UnauthorisedException {
		var user = account.current();

		if (user.isEmpty()) throw new UnauthorisedException();

		var image = new ImageForm(src, user.get());

		repository.add(image);

		return image;
	}

	public void approve(String src) throws NullPointerException {
		var o = repository.get(src);

		if (o.isEmpty()) throw new NullPointerException(
			"Cannot approve image that does not exist."
		);

		o.get().approve();
	}

	public void reject(String src) throws NullPointerException {
		var o = repository.get(src);

		if (o.isEmpty()) throw new NullPointerException(
			"Cannot approve image that does not exist."
		);

		o.get().reject();
	}

	public void remove(String src) {
		repository.remove(src);
	}

	public Collection<ImageForm> all() {
		return repository.all();
	}

	public Collection<ImageForm> allPending() {
		return all()
			.stream()
			.filter(image -> image.status().get() == ImageFormStatus.PENDING)
			.toList();
	}

	public Collection<ImageForm> allApproved() {
		return all()
			.stream()
			.filter(image -> image.status().get() == ImageFormStatus.APPROVED)
			.toList();
	}

	public Collection<ImageForm> allRejected() {
		return all()
			.stream()
			.filter(image -> image.status().get() == ImageFormStatus.REJECTED)
			.toList();
	}
}
