package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.tomgibara.algebra.Order;
import com.tomgibara.algebra.monoid.Monoid;

public interface Group<E> extends Monoid<E> {

	interface Operation<E> extends Monoid.Operation<E> {
	
		E invert(E e);
		
		default E composeInverse(E e1, E e2) {
			return compose(e1, invert(e2));
		}
		
		@Override
		public default E power(E e, long p) {
			if (p < 0) {
				return Monoid.Operation.super.power(invert(e), -p);
			} else {
				return Monoid.Operation.super.power(e, p);
			}
		}
	
		@Override
		public default E power(E e, BigInteger p) {
			if (p == null) throw new IllegalArgumentException("null p");
			if (p.signum() < 0) {
				return Monoid.Operation.super.power(invert(e), p.negate());
			} else {
				return Monoid.Operation.super.power(e, p);
			}
		}
		
	}
	
	Operation<E> op();
	
	default boolean isIdentity(E e) {
		return equality().isEquivalent(e, op().identity());
	}
	
	// note that group may still be abelian with a non-commutative operation
	// eg it could be a centralizer of a non abelian group
	// default implementation only valid for 'small' groups
	default boolean isAbelian() {
		Operation<E> op = op();
		if (!getOrder().isSmall()) return op.isCommutative();
		List<E> list = new ArrayList<E>(elements());
		int size = list.size();
		for (int i = 1; i < size; i++) {
			E ei = list.get(i);
			for (int j = 0; j < i; j++) {
				E ej = list.get(j);
				if (!commutes(ei, ej)) return false;
			}
		}
		return true;
	}
	
	default Subgroup<E> inducedSubgroup(E... es) {
		//TODO repeatedly operate new elements against all elements until no new elements
		throw new UnsupportedOperationException();
	}

	// throws an illegal state exception if order not small enough
	default Set<E> elements() {
		final int size = getOrder().asInt();
		return new AbstractSet<E>() {

			@Override
			public Iterator<E> iterator() {
				return Group.this.iterator();
			}

			@Override
			public boolean contains(Object o) {
				if (o == null) return false;
				try {
					return Group.this.contains((E) o);
				} catch (IllegalArgumentException e) {
					//TODO is there a way to test the type of o first?
					return false;
				}
			}

			@Override
			public boolean isEmpty() {
				return false;
			}

			@Override
			public int size() {
				return size;
			}
		};
	}

}
