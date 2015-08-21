package com.tomgibara.algebra.group;

import com.tomgibara.algebra.Algebra;
import com.tomgibara.collect.EquRel;
import com.tomgibara.collect.EquivalenceSet;

public interface Coset<E> extends Algebra<E> {

	public enum Side {
		LEFT (true , false),
		RIGHT(false, true ),
		BOTH (true , true );

		public final boolean left;
		public final boolean right;
		
		private Side(boolean left, boolean right) {
			this.left = left;
			this.right = right;
		}
		
		public <E> Coset<E> entireCoset(Group<E> group) {
			if (group == null) throw new IllegalArgumentException("null group");
			return new EntireCoset<>(this, group);
		}
		
		public <E> Coset<E> singletonCoset(Group<E> group, E e) {
			if (group == null) throw new IllegalArgumentException("null group");
			if (e == null) throw new IllegalArgumentException("null e");
			return new SingletonCoset<>(this, group, e);
		}
		
		public <E> Coset<E> smallCoset(EquivalenceSet<E> es) {
			if (es == null) throw new IllegalArgumentException("null es");
			if (es.isEmpty()) throw new IllegalArgumentException("empty es");
			return new SmallCoset<>(this, es);
		}
		
		public <E> Coset<E> smallCoset(Group<E> subgroup, E g) {
			if (subgroup == null) throw new IllegalArgumentException("null subgroup");
			if (g == null) throw new IllegalArgumentException("null g");
			return new SmallCoset<>(this, subgroup, g);
		}
		
		public Side of(Group<?> group) {
			if (this == BOTH) return this;
			return group.isAbelian() ? BOTH : this;
		}
	}

	Side getSide();

	E getRepresentative();

}
