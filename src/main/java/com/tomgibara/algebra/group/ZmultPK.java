package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.Iterator;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.Equivalence;

class ZmultPK implements Group<BigInteger> {

	private final BigInteger p;
	private final int k;
	private final BigInteger m; // p^k
	private final Size size; // p^k - p

	private final Operation<BigInteger> op = new Operation<BigInteger>() {

		@Override
		public boolean isCommutative() {
			return true;
		}

		@Override
		public BigInteger compose(BigInteger e1, BigInteger e2) {
			return e1.multiply(e2).remainder(m);
		}

		@Override
		public BigInteger identity() {
			return BigInteger.ONE;
		}

		@Override
		public BigInteger invert(BigInteger e) {
			return e.modInverse(m);
		}

		@Override
		public BigInteger power(BigInteger e, BigInteger p) {
			return e.modPow(p, m);
		}

		@Override
		public BigInteger power(BigInteger e, long p) {
			if (Math.abs(p) > 1) return e.modPow(BigInteger.valueOf(p), m);
			switch ((int) p) {
			case -1 : return e.modInverse(m);
			case  0 : return BigInteger.ONE;
			default : return e;
			}
		}
	};

	ZmultPK(BigInteger p, int k) {
		this.p = p;
		this.k = k;
		m = p.pow(k);
		size = Size.fromBig(m.subtract(p));
	}

	@Override
	public boolean contains(BigInteger e) {
		// not too big, not negative, not a divisor of zero
		return e.compareTo(m) < 0 && e.signum() > 0 && e.mod(p).signum() != 0;
	}

	@Override
	public Size getSize() {
		return size;
	}

	@Override
	public Equivalence<BigInteger> equality() {
		return Equivalence.equality();
	}

	@Override
	public Operation<BigInteger> op() {
		return op;
	}

	@Override
	public Iterator<BigInteger> iterator() {
		return new Iterator<BigInteger>() {
			private BigInteger next = BigInteger.ZERO;

			@Override
			public boolean hasNext() {
				return next.compareTo(size.asBig()) < 0;
			}

			@Override
			public BigInteger next() {
				BigInteger tmp = next;
				next = next.add(BigInteger.ONE);
				return tmp;
			}
		};
	}

	@Override
	public boolean isAbelian() {
		return true;
	}

	@Override
	public boolean isIdentity(BigInteger e) {
		return e.equals(BigInteger.ONE);
	}

//	@Override
//	public Size orderOf(BigInteger e) {
//		if (!contains(e)) throw new IllegalArgumentException();
//		if (isIdentity(e)) return Size.ONE;
//		int x = n;
//		while (true) {
//			BigInteger[] dr = e.divideAndRemainder(p);
//			if (dr[1].signum() != 0) break;
//			e = dr[0];
//			x--;
//		}
//		return Size.fromBig(p.pow(x).subtract(BigInteger.ONE));
//	}

}
