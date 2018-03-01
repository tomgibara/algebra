package com.tomgibara.algebra.group;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;

import com.tomgibara.collect.Equivalence;
import com.tomgibara.collect.EquivalenceSet;
import com.tomgibara.permute.Permutation;

public final class Groups {

	private static final int PRIME_CERTAINTY = 10;

	static <E> Iterator<E> alternator(E x, Group.Operation<E> op) {
		return new Iterator<E>() {
			long n = 0;
			@Override public boolean hasNext() { return true; }
			@Override public E next() { try { return op.power(x, n); } finally { n = n < 0 ? -n : -n-1; } }
		};

	}

	public static Group<BigInteger> Z() {
		return Z.z;
	}

	public static Group<BigInteger> Zn(BigInteger n) {
		if (n == null) throw new IllegalArgumentException("null n");
		if (n.signum() <= 0) throw new IllegalArgumentException("non-positive n");
		return new Zn(n);
	}

	public static Group<BigInteger> ZmultPK(BigInteger p, int k) {
		if (p == null) throw new IllegalArgumentException("null p");
		if (!p.isProbablePrime(PRIME_CERTAINTY)) throw new IllegalArgumentException("p not prime");
		if (k < 0) throw new IllegalArgumentException("negative k");
		return k == 0 ? new TrivialGroup<>(BigInteger.ONE) : new ZmultPK(p, k);
	}

	public static Group<Permutation> S(int order) {
		if (order < 0) throw new IllegalArgumentException("negative order");
		return new S(order);
	}

	public static Group<Double> Rdbl() {
		return Rdbl.instance;
	}

	public static Group<BigDecimal> Rbig() {
		return Rbig.instance;
	}

	public static <E> Group<E> from(final Group.Operation<E> op, Collection<E> elements) {
		return from(op, elements, Equivalence.equality());
	}

	public static <E> Group<E> trivial(E identity) {
		if (identity == null) throw new IllegalArgumentException("null identity");
		return new TrivialGroup<>(identity);
	}

	public static <E> Group<E> from(Group.Operation<E> op, Collection<E> elements, Equivalence<E> equality) {
		if (op == null) throw new IllegalArgumentException("null op");
		if (elements == null) throw new IllegalArgumentException("null elements");
		if (!elements.contains(op.identity())) throw new IllegalArgumentException("elements does not contain identity");
		if (equality == null) throw new IllegalArgumentException("null equality");
		return new SmallGroup<>(op, elements, equality);
	}

	public static <E> Group<E> from(Group.Operation<E> op, EquivalenceSet<E> elements) {
		if (op == null) throw new IllegalArgumentException("null op");
		if (elements == null) throw new IllegalArgumentException("null elements");
		if (!elements.contains(op.identity())) throw new IllegalArgumentException("elements does not contain identity");
		return new SmallGroup<E>(op, elements);
	}
}
