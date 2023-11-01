package com.group2.oop.voucher;

import com.group2.oop.serialisation.MapIO;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class VoucherUserRepository implements Serializable {
    private static final String USER_VOUCHER_REPOSITORY_PATH = "vouchersUser.ser";

	private HashMap<String, VoucherUserRepository > vouchersUser = new HashMap<>();
	private MapIO<String, VoucherUserRepository > io = new MapIO<>(
		vouchersUser,
		USER_VOUCHER_REPOSITORY_PATH
	);

    private String code;
    private String description;

    public VoucherUserRepository() {
    // Default constructor, doesn't require arguments.
    try {
        vouchersUser = io.read();
    } catch (IOException e) {
        System.out.println("Error reading vouchers from disk.");
        e.printStackTrace();
    }
}

    // Other constructor remains the same
    public VoucherUserRepository(String code, String description) {
        this.code = code;
        this.description = description;
    }


    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }
    
     public void add(VoucherUserRepository voucherUser) {
        vouchersUser.put(voucherUser.getCode(), voucherUser);
        write();
    }

    public Optional<VoucherUserRepository > get(String code) {
        return Optional.ofNullable(vouchersUser.get(code));
    }

    public Collection<VoucherUserRepository > all() {
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

