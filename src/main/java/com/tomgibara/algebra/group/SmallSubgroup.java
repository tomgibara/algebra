package com.tomgibara.algebra.group;

import com.tomgibara.algebra.group.Coset.Side;

class SmallSubgroup<E> implements Subgroup<E> {

	private final Group<E> group;
	private final Group<E> subgroup;

	public SmallSubgroup(Group<E> group, Group<E> subgroup) {
		this.group = group;
		this.subgroup = subgroup;
	}

	@Override
	public Group<E> getSubgroup() {
		return subgroup;
	}

	@Override
	public Group<E> getOvergroup() {
		return group;
	}

	@Override
	public Coset<E> leftCoset(E e) {
		return new SmallCoset<E>(Side.LEFT.of(group), subgroup, e);
	}

	@Override
	public Coset<E> rightCoset(E e) {
		return new SmallCoset<E>(Side.RIGHT.of(group), subgroup, e);
	}

}
