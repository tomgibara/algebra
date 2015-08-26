package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.Iterator;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.EquRel;

class Zmult implements Group<BigInteger> {

	private final BigInteger p;
	private final int n;
	private final Size size;

	private final Operation<BigInteger> op = new Operation<BigInteger>() {

		@Override
		public boolean isCommutative() {
			return true;
		}

		@Override
		public BigInteger compose(BigInteger e1, BigInteger e2) {
			return e1.multiply(e2).remainder(size.asBig());
		}

		@Override
		public BigInteger identity() {
			return BigInteger.ONE;
		}

		@Override
		public BigInteger invert(BigInteger e) {
			return e.modInverse(size.asBig());
		}

		@Override
		public BigInteger power(BigInteger e, BigInteger p) {
			return e.modPow(p, size.asBig());
		}

		@Override
		public BigInteger power(BigInteger e, long p) {
			if (p == 0) return BigInteger.ONE;
			else if (p < 0) e = e.modInverse(size.asBig());
			return e.modPow(BigInteger.valueOf(p), size.asBig());
		}
	};

	Zmult(BigInteger p, int n) {
		this.p = p;
		this.n = n;
		size = Size.fromBig(p.pow(n));
	}

	@Override
	public boolean contains(BigInteger e) {
		return e.compareTo(size.asBig()) < 0 && e.signum() > 0;
	}

	@Override
	public Size getSize() {
		return size;
	}

	@Override
	public EquRel<BigInteger> equality() {
		return EquRel.equality();
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

	@Override
	public Size orderOf(BigInteger e) {
		//TODO needs a proper implementation
		return isIdentity(e) ? Size.ONE : size;
	}

}
