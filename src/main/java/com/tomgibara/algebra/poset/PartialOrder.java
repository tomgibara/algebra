package com.tomgibara.algebra.poset;

//TODO consider moving into parent package
public interface PartialOrder<E> {

	enum Comparison {

		EQUAL, // if a<=b and b<=a
		ORDERED, // if b<=a but not a<=b
		DISORDERED, // if a<=b but not b<=a
		INCOMPARABLE; //if neither a<=b or b<=a

		//TODO how to describe this ??
		public Comparison combine(Comparison that) {
			if (this == that) return this;
			if (this == INCOMPARABLE) return this;
			if (that == INCOMPARABLE) return that;
			if (this == EQUAL) return that;
			if (that == EQUAL) return this;
			return INCOMPARABLE;
		}

	}

	default boolean isOrdered(E a, E b) {
		return compare(a, b) == Comparison.ORDERED;
	}

	default Comparison compare(E a, E b) {
		final boolean alteb = isOrdered(a, b);
		final boolean bltea = isOrdered(b, a);
		if (alteb) {
			return bltea ? Comparison.EQUAL : Comparison.DISORDERED;
		} else {
			return bltea ? Comparison.ORDERED : Comparison.INCOMPARABLE;
		}
	}

}
