package com.group2.oop.voucher;

import com.group2.oop.serialisation.MapIO;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class UserVoucherRepository implements Serializable {

	private static final String VOUCHER_USER_REPOSITORY_PATH =
		"vouchersUser.ser";

	private HashMap<String, UserVoucherRepository> vouchersUser = new HashMap<>();
	private MapIO<String, UserVoucherRepository> io = new MapIO<>(
		vouchersUser,
		VOUCHER_USER_REPOSITORY_PATH
	);

	private String code;
	private String description;

	// TODO: investigate what this does
	public UserVoucherRepository() {
		// Default constructor, doesn't require arguments.
		try {
			vouchersUser = io.read();
		} catch (IOException e) {
			System.out.println("Error reading vouchers from disk.");
			e.printStackTrace();
		}
	}

	// Other constructor remains the same
	public UserVoucherRepository(String code, String description) {
		this.code = code;
		this.description = description;
	}

	public String getCode() {
		return code;
	}

	public String getDescription() {
		return description;
	}

	public void add(UserVoucherRepository voucherUser) {
		vouchersUser.put(voucherUser.getCode(), voucherUser);
		write();
	}

	public Optional<UserVoucherRepository> get(String code) {
		return Optional.ofNullable(vouchersUser.get(code));
	}

	public Collection<UserVoucherRepository> all() {
		return vouchersUser.values();
	}

	public void write() {
		try {
			io.write();
		} catch (IOException e) {
			System.out.println("Error writing vouchers.");
			e.printStackTrace();
		}
	}

	@Override
	public String toString() {
		return "Code: " + code + ", Description: " + description;
	}
}
