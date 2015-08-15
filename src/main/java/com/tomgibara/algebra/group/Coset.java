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
			
			return new Coset<E>() {

				@Override
				public boolean contains(E e) {
					return group.contains(e);
				}

				@Override
				public EquRel<E> equality() {
					return group.equality();
				}

				@Override
				public Group<E> getGroup() {
					return group;
				}

				@Override
				public Side getSide() {
					return Side.this;
				}
				
				@Override
				public E getRepresentative() {
					return group.op().identity();
				}

			};
		}
		
		public <E> Coset<E> singletonCoset(Group<E> group, E e) {
			if (group == null) throw new IllegalArgumentException("null group");
			if (e == null) throw new IllegalArgumentException("null e");
			
			return new Coset<E>() {
				
				@Override
				public boolean contains(E el) {
					return group.equality().isEquivalent(e, el);
				}
				
				@Override
				public EquRel<E> equality() {
					return group.equality();
				}
				
				@Override
				public Group<E> getGroup() {
					return group;
				}
				
				@Override
				public Side getSide() {
					return Side.this;
				}
				
				@Override
				public E getRepresentative() {
					return e;
				}
				
			};
		}
	}

	Side getSide();

	Group<E> getGroup();

	E getRepresentative();
}
