package com.group2.oop.db;

import javax.annotation.Nullable;

public interface MutationNotifier {
	void onMutated(
		@Nullable Object k,
		@Nullable Object v,
		MutationNotifier ctx
	);
}
