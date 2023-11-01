package com.group2.oop.form;

import com.group2.oop.account.User;
import com.group2.oop.db.Effect;
import com.group2.oop.store.Store;
import com.group2.oop.store.Supply;
import java.io.Serializable;

public class ImageForm implements Serializable {

	private String src;

	public String src() {
		return src;
	}

	private User submitter;

	public User submitter() {
		return submitter;
	}

	@Effect
	private Store<ImageFormStatus> status;

	public Supply<ImageFormStatus> status() {
		return status.supply();
	}

	public ImageForm(String src, User submitter, ImageFormStatus status) {
		this.src = src;
		this.submitter = submitter;
		this.status = new Store<>(status);
	}

	public ImageForm(String src, User submitter) {
		this(src, submitter, ImageFormStatus.PENDING);
	}

	public void approve() {
		this.status.set(ImageFormStatus.APPROVED);
	}

	public void reject() {
		this.status.set(ImageFormStatus.REJECTED);
	}
}
