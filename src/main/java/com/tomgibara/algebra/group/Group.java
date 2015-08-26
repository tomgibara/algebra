package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.tomgibara.algebra.Size;
import com.tomgibara.algebra.monoid.Monoid;
import com.tomgibara.collect.Collect;
import com.tomgibara.collect.Equivalence;
import com.tomgibara.collect.EquivalenceSet;

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
		if (!getSize().isSmall()) return op.isCommutative();
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
	
	default Subgroup<E> generatedSubgroup(E... es) {
		if (es == null) throw new IllegalArgumentException("null es");
		if (es.length == 0) return Subgroup.trivialSubgroup(this);
		Equivalence<E>.Sets sets = Collect.equivalence(equality()).setsWithGenericStorage();
		//TODO want to specify capacity
		EquivalenceSet<E> set = sets.newSet(Arrays.asList(es));
		EquivalenceSet<E> prev = set;
		if (!getSize().isSmall()) throw new UnsupportedOperationException();
		// ensure we include the identity
		Operation<E> op = op();
		E id = op.identity();
		if (!prev.contains(id)) {
			prev.add(id);
		}
		// repeatedly operate new elements against all elements until no new elements
		boolean abelian = isAbelian();
		while (true) {
			EquivalenceSet<E> next = sets.newSet();
			for (E e1 : prev) {
				for (E e2 : prev) {
					E e12 = op.compose(e1, e2);
					if (!set.contains(e12)) {
						next.add(e12);
					}
					if (!abelian) {
						E e21 = op.compose(e2, e1);
						if (!set.contains(e21)) {
							next.add(e21);
						}
					}
				}
			}
			if (next.isEmpty()) break;
			set.addAll(next);
			prev = next;
		}
		// fabricate a subgroup
		return new SmallSubgroup<>(this, new SmallGroup<>(op, set));
	}

	// throws an illegal state exception if size not small enough
	default Set<E> elements() {
		final int size = getSize().asInt();
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

	default Size orderOf(E e) {
		if (!getSize().isSmall()) throw new UnsupportedOperationException();
		Operation<E> op = op();
		int order;
		for (order = 1; !isIdentity(e); order++) {
			e = op.compose(e, e);
		}
		return Size.fromInt(order);
	}
}
