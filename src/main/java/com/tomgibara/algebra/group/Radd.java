package com.tomgibara.algebra.group;

import java.util.Iterator;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.Equivalence;

abstract class Radd<E> implements Group<E> {

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

	abstract class RSubgroup implements Subgroup<E>, Group<E> {

		abstract E x();

		// subgroup methods

		@Override
		public Coset<E> rightCoset(E e) {
			if (e == null) throw new IllegalArgumentException("null e");
			return newCoset(e);
		}

		@Override
		public Coset<E> leftCoset(E e) {
			if (e == null) throw new IllegalArgumentException("null e");
			return newCoset(e);
		}

		@Override
		public Group<E> getSubgroup() {
			return this;
		}

		@Override
		public Group<E> getOvergroup() {
			return Radd.this;
		}

		abstract RCoset newCoset(E r);

		abstract class RCoset implements Coset<E> {

			@Override
			public Side getSide() {
				return Side.BOTH;
			}

			@Override
			public Equivalence<E> equality() {
				return Radd.this.equality();
			}

			@Override
			public Size getSize() {
				return Size.COUNTABLY_INFINITE;
			}

		}

		// group methods

		@Override
		public Size getSize() {
			return Size.COUNTABLY_INFINITE;
		}

		@Override
		public Equivalence<E> equality() {
			return Radd.this.equality();
		}

		@Override
		public Iterator<E> iterator() {
			return Groups.alternator(x(), Radd.this.op());
		}

		@Override
		public Operation<E> op() {
			return Radd.this.op();
		}

	}
}
