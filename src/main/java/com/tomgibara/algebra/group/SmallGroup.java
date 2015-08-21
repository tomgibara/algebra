package com.tomgibara.algebra.group;

import java.util.Collection;
import java.util.Iterator;
import java.util.Set;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.Collect;
import com.tomgibara.collect.EquRel;
import com.tomgibara.collect.EquivalenceSet;

class SmallGroup<E> implements Group<E> {

	private final Operation<E> op;
	private final EquivalenceSet<E> els;
	private final Size size;
	private Boolean abelian = null;

	SmallGroup(Group.Operation<E> op, Collection<E> elements, EquRel<E> equality) {
		this(op, Collect.equivalence(equality).setsWithGenericStorage().newSet(elements));
	}
	
	SmallGroup(Group.Operation<E> op, EquivalenceSet<E> els) {
		this.op = op;
		this.els = els.immutable();
		size = Size.fromLong(els.size());
	}
	
	@Override
	public EquRel<E> equality() {
		return els.getEquivalence();
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

}
