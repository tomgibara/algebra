package com.tomgibara.algebra.group;

import java.util.Collections;

import com.tomgibara.collect.EquRel;

public interface Subgroup<E> {

	static <E> Subgroup<E> totalSubgroup(Group<E> group) {
		return new Subgroup<E>() {

			private final Coset<E> coset = Coset.Side.BOTH.entireCoset(group);

			@Override
			public Group<E> getSubgroup() {
				return group;
			}

			@Override
			public Group<E> getOvergroup() {
				return group;
			}

			@Override
			public Coset<E> leftCoset(E e) {
				return coset;
			}

			@Override
			public Coset<E> rightCoset(E e) {
				return coset;
			}
		};
	}

	static <E> Subgroup<E> trivialSubgroup(Group<E> group) {
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

	static <E> Subgroup<E> smallSubgroup(final Group<E> group, final Group<E> subgroup) {
		if (group == null) throw new IllegalArgumentException("null group");
		if (subgroup == null) throw new IllegalArgumentException("null subgroup");
		if (!group.getSize().isSmall()) throw new IllegalArgumentException("large group not supported");
		if (!group.equality().equals(subgroup.equality())) throw new IllegalArgumentException("equality differs between group and subgroup");
		E id1 = group.op().identity();
		E id2 = subgroup.op().identity();
		if (!group.equality().isEquivalent(id1, id2)) throw new IllegalArgumentException("identity differs between group and subgroup");
		return new SmallSubgroup<E>(group, subgroup);
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
