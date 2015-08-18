package com.tomgibara.algebra.group;

import com.tomgibara.algebra.Algebra;
import com.tomgibara.collect.EquRel;

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
			return new EntireCoset<E>(this, group);
		}
		
		public <E> Coset<E> singletonCoset(Group<E> group, E e) {
			if (group == null) throw new IllegalArgumentException("null group");
			if (e == null) throw new IllegalArgumentException("null e");
			return new SingletonCoset<E>(this, group, e);
		}
	}

	Side getSide();

	E getRepresentative();

}
