package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.Collect;
import com.tomgibara.collect.EquRel;

public class Groups {

	public static final Group<BigInteger> Z = com.tomgibara.algebra.group.Z.z;

	public static final <E> Group<E> from(final Group.Operation<E> op, Collection<E> elements) {
		return from(op, elements, EquRel.equality());
	}

	//TODO doesn't close over elements - should it?
	public static final <E> Group<E> from(final Group.Operation<E> op, Collection<E> elements, EquRel<E> equality) {
		if (op == null) throw new IllegalArgumentException("null op");
		if (elements == null) throw new IllegalArgumentException("null elements");
		if (!elements.contains(op.identity())) throw new IllegalArgumentException("elements does not contain identity");
		if (equality == null) throw new IllegalArgumentException("null equality");

		return new Group<E>() {

			private final Set<E> els = Collect.equivalence(equality).setsWithGenericStorage().newSet(elements);
			private final Size size = Size.fromLong(elements.size());
			private Boolean abelian = null;

			@Override
			public EquRel<E> equality() {
				return equality;
			}

			@Override
			public Iterator<E> iterator() {
				return els.iterator();
			}

			@Override
			public boolean contains(E e) {
				return els.contains(e);
			}

			@Override
			public Set<E> elements() {
				return els;
			}

			@Override
			public Group.Operation<E> op() {
				return op;
			}

			@Override
			public boolean isAbelian() {
				if (abelian == null) {
					abelian = Group.super.isAbelian();
				}
				return abelian;
			}

			@Override
			public Size getSize() {
				return size;
			}

		};
	}

}
