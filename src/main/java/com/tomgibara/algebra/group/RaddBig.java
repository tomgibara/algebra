package com.tomgibara.algebra.group;

import java.math.BigDecimal;
import java.math.BigInteger;

import com.tomgibara.collect.Equivalence;

public class RaddBig extends Radd<BigDecimal> {

	private static final Operation<BigDecimal> op = new Operation<BigDecimal>() {

		@Override public boolean isCommutative() { return true; }

		@Override public BigDecimal identity()                                   { return BigDecimal.ZERO;                   }
		@Override public BigDecimal compose(BigDecimal e1, BigDecimal e2)        { return e1.add(e2);                        }
		@Override public BigDecimal composeInverse(BigDecimal e1, BigDecimal e2) { return e1.subtract(e2);                   }
		@Override public BigDecimal invert(BigDecimal e)                         { return e.negate();                        }
		@Override public BigDecimal power(BigDecimal e, long p)                  { return e.multiply(BigDecimal.valueOf(p)); }
		@Override public BigDecimal power(BigDecimal e, BigInteger p)            { return e.multiply(new BigDecimal(p));     }
	};

	static final RaddBig instance = new RaddBig();

	private RaddBig() { }

	@Override
	public Operation<BigDecimal> op() {
		return op;
	}

	@Override
	public Equivalence<BigDecimal> equality() {
		return Equivalence.bigDecimal();
	}

	@Override
	public boolean contains(BigDecimal e) {
		if (e == null) throw new IllegalArgumentException("null e");
		return true;
	}

	@Override
	public Subgroup<BigDecimal> generatedSubgroup(BigDecimal... es) {
		if (es == null) throw new IllegalArgumentException("null es");
		switch (es.length) {
		case 1 :
			BigDecimal e = es[0];
			if (e == null) throw new IllegalArgumentException("null e");
			if (e.signum() != 0) return new Multiple(es);
			// fall through
		case 0 :
			return Subgroup.trivialSubgroup(this);
		default: return new Multiple(es);
		}
	}

	private class Multiple extends Radd<BigDecimal>.RSubgroup {

		private final BigDecimal x;

		public Multiple(BigDecimal... xs) {
			// first identify the greatest scale
			int s = xs[0].scale();
			for (int i = 1; i < xs.length; i++) {
				s = Math.max(s, xs[i].scale());
			}
			// compute the gcd as integers
			BigInteger gcd = null;
			for (int i = 0; i < xs.length; i++) {
				BigInteger x = xs[i].scaleByPowerOfTen(s).toBigIntegerExact();
				gcd = gcd == null ? x : gcd.gcd(x);
			}
			// scale to restore decimal value
			x = new BigDecimal(gcd, s);
		}

		@Override
		BigDecimal x() {
			return x;
		}

		@Override
		Radd<BigDecimal>.RSubgroup.RCoset newCoset(BigDecimal r) {
			if (r == null) throw new IllegalArgumentException("null r");
			return new RCoset() {

				@Override
				public boolean contains(BigDecimal e) {
					if (e == null) throw new IllegalArgumentException("null e");
					return e.subtract(r).remainder(x).signum() == 0;
				}

				@Override
				public BigDecimal getRepresentative() {
					return r;
				}
			};
		}

		@Override
		public Equivalence<BigDecimal> equality() {
			return Equivalence.bigDecimal();
		}

		@Override
		public boolean contains(BigDecimal e) {
			if (e == null) throw new IllegalArgumentException("null e");
			return e.remainder(x).signum() == 0;
		}

		@Override
		public Subgroup<BigDecimal> generatedSubgroup(BigDecimal... es) {
			for (BigDecimal e : es) {
				if (!contains(e)) throw new IllegalArgumentException("invalid e");
			}
			return es.length == 0 ? Subgroup.trivialSubgroup(this) : new Multiple(es);
		}
	}
}
