package com.tomgibara.algebra.field;

import com.tomgibara.algebra.group.Group;
import com.tomgibara.algebra.ring.Ring;

public interface Field<E> extends Ring<E> {

	@Override
	Group<E> multiplication();

}
