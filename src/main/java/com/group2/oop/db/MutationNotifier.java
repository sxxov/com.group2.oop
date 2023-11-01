package com.group2.oop.db;

import javax.annotation.Nullable;

public interface MutationNotifier {
	void onMutate(@Nullable Object k, @Nullable Object v, MutationNotifier ctx);
}
