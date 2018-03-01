package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.Collections;
import java.util.Iterator;
import java.util.Set;
import java.util.function.Consumer;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.Equivalence;

class TrivialGroup<E> implements Group<E> {

	private final E el;
	private final Operation<E> op = new Operation<E>() {

		@Override
		public boolean isCommutative() {
			return true;
		}

		@Override
		public E compose(E e1, E e2) {
			checkValid(e1);
			checkValid(e2);
			return e1;
		}

		@Override
		public E identity() {
			return el;
		}

		@Override
		public E invert(Object e) {
			return el;
		}

		@Override
		public E composeInverse(E e1, E e2) {
			return compose(e1, e2);
		}

		@Override
		public E power(E e, BigInteger p) {
			if (p == null) throw new IllegalArgumentException("null p");
			return e;
		}

		@Override
		public E power(E e, long p) {
			return e;
		}

	};

	TrivialGroup(E el) {
		this.el = el;
	}

	@Override
	public Size getSize() {
		return Size.ONE;
	}

	@Override
	public boolean contains(E e) {
		if (e == null) throw new IllegalArgumentException("null e");
		return el.equals(e);
	}

	@Override
	public Equivalence<E> equality() {
		return Equivalence.equality();
	}

	@Override
	public Iterator<E> iterator() {
		return Collections.singleton(el).iterator();
	}

	@Override
	public Operation<E> op() {
		return op;
	}

	@Override
	public void forEach(Consumer<? super E> action) {
		action.accept(el);
	}

	@Override
	public Set<E> elements() {
		return Collections.singleton(el);
	}

	@Override
	public boolean isAbelian() {
		return true;
	}

	@Override
	public boolean isIdentity(E e) {
		checkValid(e);
		return true;
	}

	@Override
	public Size orderOf(E e) {
		checkValid(e);
		return Size.ONE;
	}

	@Override
	public boolean commutes(E e1, E e2) {
		checkValid(e1);
		checkValid(e2);
		return true;
	}

	@Override
	public Subgroup<E> generatedSubgroup(E... es) {
		return Subgroup.totalSubgroup(this);
	}

	private void checkValid(E e) {
		if (!contains(e)) throw new IllegalArgumentException("invalid element");
	}

}
