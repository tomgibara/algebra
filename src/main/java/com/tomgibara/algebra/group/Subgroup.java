package com.tomgibara.algebra.group;

import java.util.Collections;

import com.tomgibara.collect.EquRel;

public interface Subgroup<E> {

	static <E> Subgroup<E> identitySubgroup(Group<E> group) {
		Group<E> identity = Groups.from(group.op(), Collections.singleton(group.op().identity()));
		return new Subgroup<E>() {

			@Override
			public Group<E> getSubgroup() {
				return identity;
			}

			@Override
			public Group<E> getOvergroup() {
				return group;
			}

			@Override
			public Coset<E> leftCoset(E e) {
				return (group.isAbelian() ? Coset.Side.BOTH : Coset.Side.LEFT).singletonCoset(group, e);
			}

			@Override
			public Coset<E> rightCoset(E e) {
				return (group.isAbelian() ? Coset.Side.BOTH : Coset.Side.LEFT).singletonCoset(group, e);
			}
		};
	}

	Group<E> getSubgroup();
	
	Group<E> getOvergroup();
	
	//TODO replace with coset that takes side parameter?
	Coset<E> leftCoset(E e);

	Coset<E> rightCoset(E e);

	default EquRel<E> leftCosetEquivalence() {
		return new EquRel<E>() {
			public boolean isEquivalent(E e1, E e2) {
				return leftCoset(e1).contains(e2);
			}
		};
	}

	default EquRel<E> rightCosetEquivalence() {
		return new EquRel<E>() {
			public boolean isEquivalent(E e1, E e2) {
				return rightCoset(e1).contains(e2);
			}
		};
	}

}
