package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.Collect;
import com.tomgibara.collect.EquRel;
import com.tomgibara.collect.EquivalenceSet;
import com.tomgibara.permute.Permutation;

public class Groups {

	private static final int PRIME_CERTAINTY = 10;
	
	public static final Group<BigInteger> Z() {
		return Z.z;
	}
	
	public static final Group<BigInteger> Zn(BigInteger n) {
		if (n == null) throw new IllegalArgumentException("null n");
		if (n.signum() <= 0) throw new IllegalArgumentException("non-positive n");
		return new Zn(n);
	}
	
	public static final Group<BigInteger> Zmult(BigInteger p, int n) {
		if (p == null) throw new IllegalArgumentException("null p");
		if (!p.isProbablePrime(PRIME_CERTAINTY)) throw new IllegalArgumentException("p not prime");
		if (n < 1) throw new IllegalArgumentException("n not positive");
		return new Zmult(p, n);
	}

	public static final S S(int order) {
		if (order < 0) throw new IllegalArgumentException("negative order");
		return new S(order);
	}

	public static final <E> Group<E> from(final Group.Operation<E> op, Collection<E> elements) {
		return from(op, elements, EquRel.equality());
	}

	public static final <E> Group<E> from(Group.Operation<E> op, Collection<E> elements, EquRel<E> equality) {
		if (op == null) throw new IllegalArgumentException("null op");
		if (elements == null) throw new IllegalArgumentException("null elements");
		if (!elements.contains(op.identity())) throw new IllegalArgumentException("elements does not contain identity");
		if (equality == null) throw new IllegalArgumentException("null equality");
		return new SmallGroup<>(op, elements, equality);
	}

	public static final <E> Group<E> from(Group.Operation<E> op, EquivalenceSet<E> elements) {
		if (op == null) throw new IllegalArgumentException("null op");
		if (elements == null) throw new IllegalArgumentException("null elements");
		if (!elements.contains(op.identity())) throw new IllegalArgumentException("elements does not contain identity");
		return new SmallGroup<E>(op, elements);
	}
}
