package com.tomgibara.algebra.group;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.Equivalence;

class SingletonCoset<E> extends AbstractCoset<E> {

	private final Side side;
	private final Group<E> group;
	private final E r;

	SingletonCoset(Side side, Group<E> group, E r) {
		this.side = group.isAbelian() ? Side.BOTH : side;
		this.group = group;
		this.r = r;
	}

	@Override
	public Size getSize() {
		return Size.ONE;
	}

	@Override
	public boolean contains(E e) {
		return group.equality().isEquivalent(r, e);
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
		return r;
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof SingletonCoset)) return false;
		SingletonCoset<?> that = (SingletonCoset<?>) obj;
		if (this.side != that.side) return false;
		if (!this.group.equals(that.group)) return false;
		if (!group.equality().isEquivalent(this.r, (E) that.r));
		return true;
	}

}
