package com.group2.oop.form;

import com.group2.oop.db.Repository;
import com.group2.oop.db.SubRepository;
import com.group2.oop.db.drill.MapDrill1;
import com.group2.oop.db.drill.MapDrill2;
import java.util.UUID;

public class ImageFormRepository
	extends Repository<UUID, SubRepository<String, ImageForm>> {

	public MapDrill1<String, ImageForm> drill(UUID k) {
		return new MapDrill2<>(this).drill(k);
	}
}
