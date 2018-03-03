package com.tomgibara.algebra.field;

import com.tomgibara.algebra.Size;
import com.tomgibara.algebra.group.Group;
import com.tomgibara.algebra.group.Groups;
import com.tomgibara.collect.Equivalence;

class Rdbl implements Field<Double> {

	public static final Rdbl instance = new Rdbl();

	private Rdbl() {}

	@Override
	public Group<Double> addition() {
		return Groups.RaddDbl();
	}

	@Override
	public Group<Double> multiplication() {
		return Groups.RmultDbl();
	}

	@Override
	public boolean contains(Double e) {
		if (e == null) throw new IllegalArgumentException("null e");
		return true;
	}

	@Override
	public Equivalence<Double> equality() {
		return Equivalence.equality();
	}

	@Override
	public Size getSize() {
		return Size.UNCOUNTABLY_INFINITE;
	}

}
