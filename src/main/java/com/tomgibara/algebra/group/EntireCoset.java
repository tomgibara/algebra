package com.tomgibara.algebra.group;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.Equivalence;

class EntireCoset<E> extends AbstractCoset<E> {

	private final Side side;
	private final Group<E> group;

	EntireCoset(Side side, Group<E> group) {
		this.side = group.isAbelian() ? Side.BOTH : side;
		this.group = group;
	}

	@Override
	public Size getSize() {
		return Size.COUNTABLY_INFINITE;
	}

	@Override
	public boolean contains(E e) {
		return group.contains(e);
	}

	@Override
	public Equivalence<E> equality() {
		return group.equality();
	}

	@Override
	public Side getSide() {
		return side;
	}

	@Override
	public E getRepresentative() {
		return group.op().identity();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof EntireCoset)) return false;
		EntireCoset<?> that = (EntireCoset<?>) obj;
		return this.group.equals(that.group);
	}

}
