package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.Iterator;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.EquRel;

class Z extends Z_ {

	// statics

	static Z z = new Z(BigInteger.ONE);

	private static final Operation<BigInteger> op = new Operation<BigInteger>() {

		@Override
		public boolean isCommutative() {
			return true;
		}

		@Override
		public BigInteger identity() {
			return BigInteger.ZERO;
		}

		@Override
		public BigInteger compose(BigInteger e1, BigInteger e2) {
			return e1.add(e2);
		}

		@Override
		public BigInteger invert(BigInteger e) {
			return e.negate();
		}
	};

	@Override
	public Iterator<BigInteger> iterator() {
		return new Iterator<BigInteger>() {

			private BigInteger next = BigInteger.ZERO;

			@Override
			public boolean hasNext() {
				return true;
			}

			@Override
			public BigInteger next() {
				BigInteger r = next;
				if (next.signum() >= 0) next = next.add(BigInteger.ONE);
				next = next.negate();
				return r;
			}
		};
	}

	// constructors

	private Z(BigInteger g) {
		super(g, ZCosetEqu.forGenerator(g));
	}

	// methods

	@Override
	public Operation<BigInteger> op() {
		return op;
	}

	@Override
	public boolean contains(BigInteger e) {
		//TODO need to decide on the correct approach for nulls
		return e != null && containsImpl(e);
	}

	@Override
	public EquRel<BigInteger> equality() {
		return EquRel.equality();
	}

	@Override
	public Size getSize() {
		return Size.COUNTABLY_INFINITE;
	}

	@Override
	public Subgroup<BigInteger> generatedSubgroup(BigInteger... es) {
		if (es == null) throw new IllegalArgumentException("null es");
		return generated(es);
	}

	@Override
	Z_ subgroup(BigInteger g) {
		return new Z(g);
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof Z)) return false;
		Z that = (Z) obj;
		return this.g.equals(that.g);
	}

}
