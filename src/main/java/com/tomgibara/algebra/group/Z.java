package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.EquRel;

class Z implements Group<BigInteger> {

	// statics
	
	static Z z = new Z(BigInteger.ONE);
	
	static final BigInteger gcd(BigInteger... es) {
		return Arrays.stream(es).filter(e -> e.signum() != 0).map(e -> e.abs()).distinct().reduce((a,b) -> a.gcd(b)).orElse(BigInteger.ZERO);
	}
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
	
	// fields
	
	final BigInteger g;
	final EquRel<BigInteger> cosetEqu;
	
	// constructors
	
	private Z(BigInteger g) {
		this.g = g;
		this.cosetEqu = ZCosetEqu.forGenerator(g);
	}
	
	// methods

	@Override
	public Operation<BigInteger> op() {
		return op;
	}
	
	@Override
	public boolean contains(BigInteger e) {
		//TODO need to decide on the correct approach for this
		if (e == null) return false;
		if (g == BigInteger.ONE) return true;
		if (e.remainder(g).signum() == 0) return true;
		return false;
	}

	@Override
	public EquRel<BigInteger> equality() {
		return EquRel.equality();
	}

	@Override
	public boolean isAbelian() {
		return true;
	}

	@Override
	public Size getSize() {
		return Size.COUNTABLY_INFINITE;
	}

	@Override
	public Subgroup<BigInteger> inducedSubgroup(BigInteger... es) {
		if (es == null) throw new IllegalArgumentException("null es");
		BigInteger gcd = gcd(es);
		if (gcd.signum() == 0) return Subgroup.trivialSubgroup(this);
		if (gcd.equals(g)) return Subgroup.totalSubgroup(this);
		if (gcd.remainder(g).signum() != 0) throw new IllegalArgumentException("non-element");
		return new Subgroup<BigInteger>() {

			private final Z subgroup  = new Z(gcd);

			@Override
			public Group<BigInteger> getOvergroup() {
				return Z.this;
			}

			@Override
			public Group<BigInteger> getSubgroup() {
				return subgroup;
			}

			@Override
			public Coset<BigInteger> rightCoset(BigInteger e) {
				return new ZCoset(Z.this, e.remainder(subgroup.g));
			}

			@Override
			public Coset<BigInteger> leftCoset(BigInteger e) {
				return rightCoset(e);
			}

		};
	}
	
	@Override
	public Size orderOf(BigInteger e) {
		//TODO should test containment?
		return e.signum() == 0 ? Size.ONE : Size.COUNTABLY_INFINITE;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof Z)) return false;
		Z that = (Z) obj;
		return this.g.equals(that.g);
	}
	
}
