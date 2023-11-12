package com.group2.oop.form;

import com.group2.oop.account.UnauthorisedException;
import com.group2.oop.account.UserRepository;
import com.group2.oop.dependency.D;
import java.util.ArrayList;
import java.util.Collection;
import java.util.UUID;

public class ImageFormManager {

	private final ImageFormRepository repository = D.get(
		ImageFormRepository.class
	);

	private final UserRepository users = D.get(UserRepository.class);

	public ImageForm submit(UUID uuid, String src)
		throws UnauthorisedException {
		var image = new ImageForm(src, uuid);

		repository
			.drill(uuid) //
			.drill(image.src())
			.put(image);

		return image;
	}

	public void approve(UUID uuid, String src) throws NullPointerException {
		var o = repository.drill(uuid).drill(src);

		if (o.isEmpty()) throw new NullPointerException(
			"Cannot approve image that does not exist in the repository."
		);

		o.get().approve();
	}

	public void reject(UUID uuid, String src) throws NullPointerException {
		var o = repository.drill(uuid).drill(src);

		if (o.isEmpty()) throw new NullPointerException(
			"Cannot reject image that does not exist in the repository."
		);

		o.get().reject();
	}

	public void remove(UUID uuid, String src) {
		repository.drill(uuid).drill(src).remove();
	}

	public Collection<ImageForm> allEveryone() {
		var forms = new ArrayList<ImageForm>();

		for (var user : users.values()) {
			forms.addAll(repository.drill(user.uuid()).ref().values());
		}

		return forms;
	}

	public Collection<ImageForm> all(UUID uuid) {
		return repository.drill(uuid).ref().values();
	}

	public Collection<ImageForm> allEveryonePending() {
		return allEveryone()
			.stream()
			.filter(image -> image.status().get() == ImageFormStatus.PENDING)
			.toList();
	}

	public Collection<ImageForm> allPending(UUID uuid) {
		return all(uuid)
			.stream()
			.filter(image -> image.status().get() == ImageFormStatus.PENDING)
			.toList();
	}

	public Collection<ImageForm> allEveryoneApproved() {
		return allEveryone()
			.stream()
			.filter(image -> image.status().get() == ImageFormStatus.APPROVED)
			.toList();
	}

	public Collection<ImageForm> allApproved(UUID uuid) {
		return all(uuid)
			.stream()
			.filter(image -> image.status().get() == ImageFormStatus.APPROVED)
			.toList();
	}

	public Collection<ImageForm> allEveryoneRejected() {
		return allEveryone()
			.stream()
			.filter(image -> image.status().get() == ImageFormStatus.REJECTED)
			.toList();
	}

	public Collection<ImageForm> allRejected(UUID uuid) {
		return all(uuid)
			.stream()
			.filter(image -> image.status().get() == ImageFormStatus.REJECTED)
			.toList();
	}
}
