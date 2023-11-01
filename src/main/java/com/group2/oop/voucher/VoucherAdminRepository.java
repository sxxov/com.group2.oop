package com.group2.oop.voucher;

import com.group2.oop.serialisation.MapIO;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.Optional;

public class VoucherAdminRepository implements Serializable {
    private static final String FROM_VOUCHER_REPOSITORY_PATH = "vouchersAdmin.ser";

	private HashMap<String, VoucherAdminRepository> vouchers = new HashMap<>();
	private MapIO<String, VoucherAdminRepository> io = new MapIO<>(
		vouchers,
		FROM_VOUCHER_REPOSITORY_PATH
	);

    private String code;
    private String description;
    private int creditsRequired;
    
    public VoucherAdminRepository() {
    // Default constructor, doesn't require arguments.
    try {
        vouchers = io.read();
    } catch (IOException e) {
        System.out.println("Error reading vouchers from disk.");
        e.printStackTrace();
    }
}

    // Other constructor remains the same
    public VoucherAdminRepository(String code, String description, int creditsRequired) {
        this.code = code;
        this.description = description;
        this.creditsRequired = creditsRequired;
    }


    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public int getCreditsRequired() {
        return creditsRequired;
    }
    
     public void add(VoucherAdminRepository voucher) {
        vouchers.put(voucher.getCode(), voucher);
        write();
    }

    public Optional<VoucherAdminRepository> get(String code) {
        return Optional.ofNullable(vouchers.get(code));
    }

    public Collection<VoucherAdminRepository> all() {
        return vouchers.values();
    }

    public void remove(String code) {
        vouchers.remove(code);
        write();
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
        return "Code: " + code + ", Description: " + description + ", Credits Required: " + creditsRequired;
    }

}
