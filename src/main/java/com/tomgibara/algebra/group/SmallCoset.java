package com.tomgibara.algebra.group;

import com.tomgibara.algebra.Size;
import com.tomgibara.algebra.group.Group.Operation;
import com.tomgibara.collect.Collect;
import com.tomgibara.collect.EquRel;
import com.tomgibara.collect.EquivalenceSet;

class SmallCoset<E> implements Coset<E> {

	private static <E> EquivalenceSet<E> elementsFrom(Side side, Group<E> subgroup, E g) {
		EquivalenceSet<E> set = Collect.equivalence(subgroup.equality()).setsWithGenericStorage().newSet();
		Operation<E> op = subgroup.op();
		if (side.left) {
			for (E e : subgroup) {
				set.add(op.compose(g, e));
			}
		} else {
			for (E e : subgroup) {
				set.add(op.compose(e, g));
			}
		}
		return set;
	}
	
	private final Side side;
	private final EquivalenceSet<E> elements;
	
	SmallCoset(Side side, EquivalenceSet<E> elements) {
		this.side = side;
		this.elements = elements;
	}
	
	SmallCoset(Side side, Group<E> subgroup, E e) {
		this.side = side;
		this.elements = elementsFrom(side, subgroup, e);
	}
	
	@Override
	public boolean contains(E e) {
		return elements.contains(e);
	}

	@Override
	public EquRel<E> equality() {
		return elements.getEquivalence();
	}

	@Override
	public Size getSize() {
		return Size.fromInt(elements.size());
	}

	@Override
	public Side getSide() {
		return side;
	}

	@Override
	public E getRepresentative() {
		return elements.iterator().next();
	}

}
