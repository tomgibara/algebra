package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.Iterator;

import com.tomgibara.algebra.Order;
import com.tomgibara.collect.EquRel;
import com.tomgibara.hashing.HashCode;
import com.tomgibara.hashing.HashSize;
import com.tomgibara.hashing.Hasher;

class Z implements Group<BigInteger> {

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
				if (next.signum() <= 0) next = next.add(BigInteger.ONE);
				next = next.negate();
				return r;
			}
		};
	}
	
	// fields
	
	private final BigInteger g;
	private final EquRel<BigInteger> cosetEqu;
	
	// constructors
	
	private Z(BigInteger g) {
		this.g = g;
		this.cosetEqu = new EquRel<BigInteger>() {

			@Override
			public boolean isEquivalent(BigInteger e1, BigInteger e2) {
				return e1.subtract(e2).remainder(g).signum() == 0;
			}

			@Override
			public Hasher<BigInteger> getHasher() {
				return new Hasher<BigInteger>() {
					@Override
					public HashSize getSize() {
						return HashSize.INT_SIZE;
					}
					@Override
					public HashCode hash(BigInteger value) {
						return HashCode.fromInt(intHashValue(value));
					}
					@Override
					public int intHashValue(BigInteger value) throws IllegalArgumentException {
						if (value == null) throw new IllegalArgumentException("null value");
						return value.remainder(g).hashCode();
					}
				};
			}

		};

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
	public Order getOrder() {
		return Order.COUNTABLY_INFINITE;
	}

	@Override
	public Subgroup<BigInteger> inducedSubgroup(BigInteger... es) {
		if (es == null) throw new IllegalArgumentException("null es");
		BigInteger gcd = gcd(es);
		if (gcd.signum() == 0) return Subgroup.identitySubgroup(this);
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
				return new ZCoset(gcd == BigInteger.ONE ? e : e.remainder(gcd));
			}
			
			@Override
			public Coset<BigInteger> leftCoset(BigInteger e) {
				return rightCoset(e);
			}
			
		};
	}
	
	private static final BigInteger gcd(BigInteger... es) {
		int length = es.length;
		switch (length) {
		case 0 : return BigInteger.ZERO;
		case 1 : return es[0];
		case 2 : return es[0].gcd(es[1]);
		default:
			BigInteger gcd = es[0];
			for (int i = 1; i < length; i++) {
				gcd = gcd.gcd(es[i]);
			}
			return gcd;
		}
	}
	
	private class ZCoset implements Coset<BigInteger> {

		private final BigInteger r;
		
		public ZCoset(BigInteger r) {
			this.r = r;
		}
		
		@Override
		public boolean contains(BigInteger e) {
			return equality().isEquivalent(r, e);
		}

		@Override
		public EquRel<BigInteger> equality() {
			return cosetEqu;
		}

		@Override
		public Side getSide() {
			return Side.BOTH;
		}

		@Override
		public Group<BigInteger> getGroup() {
			return Z.this;
		}

		@Override
		public BigInteger getRepresentative() {
			return r;
		}
		
		
	}

}
