package com.tomgibara.algebra.field;

import java.math.BigDecimal;

import com.tomgibara.algebra.Size;
import com.tomgibara.algebra.group.Group;
import com.tomgibara.algebra.group.Groups;
import com.tomgibara.collect.Equivalence;

class Rbig implements Field<BigDecimal> {

	private final Group<BigDecimal> mult;

	Rbig(Group<BigDecimal> mult) {
		this.mult = mult;
	}

	@Override
	public Group<BigDecimal> addition() {
		return Groups.RaddBig();
	}

	@Override
	public Group<BigDecimal> multiplication() {
		return mult;
	}

	@Override
	public boolean contains(BigDecimal e) {
		if (e == null) throw new IllegalArgumentException("null e");
		return true;
	}

	@Override
	public Equivalence<BigDecimal> equality() {
		return Equivalence.equality();
	}

	@Override
	public Size getSize() {
		return Size.UNCOUNTABLY_INFINITE;
	}

}
