package com.tomgibara.algebra.group;

import java.util.Iterator;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.Equivalence;

abstract class Rmult<E> implements Group<E> {

	@Override
	public Size getSize() {
		return Size.UNCOUNTABLY_INFINITE;
	}

	@Override
	public Equivalence<E> equality() {
		return Equivalence.equality();
	}

	@Override
	public Iterator<E> iterator() {
		throw new UnsupportedOperationException("uncountable");
	}

	@Override
	public Size orderOf(E e) {
		return Size.COUNTABLY_INFINITE;
	}

}
