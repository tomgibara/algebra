package com.tomgibara.algebra.group;

abstract class AbstractCoset<E> implements Coset<E> {

	@Override
	public int hashCode() {
		return equality().getHasher().intHashValue(getRepresentative());
	}
}
