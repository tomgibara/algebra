package com.tomgibara.algebra.group;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Collection;

import com.tomgibara.collect.Equivalence;
import com.tomgibara.collect.EquivalenceSet;
import com.tomgibara.permute.Permutation;

public final class Groups {

	private static final int PRIME_CERTAINTY = 10;

	public static Group<BigInteger> Z() {
		return Z.z;
	}

	public static Group<BigInteger> Zn(BigInteger n) {
		if (n == null) throw new IllegalArgumentException("null n");
		if (n.signum() <= 0) throw new IllegalArgumentException("non-positive n");
		return new Zn(n);
	}

	public static Group<BigInteger> Zmult(BigInteger p, int n) {
		if (p == null) throw new IllegalArgumentException("null p");
		if (!p.isProbablePrime(PRIME_CERTAINTY)) throw new IllegalArgumentException("p not prime");
		if (n < 1) throw new IllegalArgumentException("n not positive");
		return new Zmult(p, n);
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
