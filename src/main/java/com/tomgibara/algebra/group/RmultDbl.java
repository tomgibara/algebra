package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.Iterator;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.Equivalence;

class RmultDbl extends Rmult<Double> {

	private static final Operation<Double> op = new Operation<Double>() {

		@Override public boolean isCommutative() { return true; }

		@Override public Double identity()                           { return 1.0;                          }
		@Override public Double compose(Double e1, Double e2)        { return e1 * e2;                      }
		@Override public Double composeInverse(Double e1, Double e2) { return e1 / e2;                      }
		@Override public Double invert(Double e)                     { return 1.0 / e;                      }
		@Override public Double power(Double e, long p)              { return Math.pow(e, p);               }
		@Override public Double power(Double e, BigInteger p)        { return Math.pow(e, p.doubleValue()); }

	};

	static final RmultDbl instance = new RmultDbl();

	private RmultDbl() { }

	@Override
	public Operation<Double> op() {
		return op;
	}

	@Override
	public boolean isIdentity(Double e) {
		if (e == null) throw new IllegalArgumentException("null e");
		return e == 1.0;
	}

	@Override
	public boolean contains(Double e) {
		if (e == null) throw new IllegalArgumentException("null e");
		return e != 0.0;
	}

	@Override
	public Subgroup<Double> generatedSubgroup(Double... es) {
		if (es == null) throw new IllegalArgumentException("null es");
		switch (es.length) {
		case 0 : return Subgroup.trivialSubgroup(this);
		case 1: return new Single(es[0]);
		default: throw new UnsupportedOperationException("unsupported");
		}
	}

	private final class Single implements Subgroup<Double>, Group<Double> {

		private final double x;
		private final double logX;

		Single(double x) {
			this.x = x;
			logX = Math.log(x);
		}

		// subgroup methods

		@Override
		public Group<Double> getSubgroup() {
			return this;
		}

		@Override
		public Group<Double> getOvergroup() {
			return RmultDbl.this;
		}

		@Override
		public Coset<Double> leftCoset(Double e) {
			if (e == null) throw new IllegalArgumentException("null e");
			return new RmultCoset(e);
		}

		@Override
		public Coset<Double> rightCoset(Double e) {
			if (e == null) throw new IllegalArgumentException("null e");
			return new RmultCoset(e);
		}

		// group methods

		@Override
		public Size getSize() {
			return Size.COUNTABLY_INFINITE;
		}

		@Override
		public boolean contains(Double e) {
			return Math.log(e) % logX == 0.0;
		}

		@Override
		public Equivalence<Double> equality() {
			return Equivalence.equality();
		}

		@Override
		public Iterator<Double> iterator() {
			return Groups.alternator(x, op);
		}

		@Override
		public Operation<Double> op() {
			return op;
		}

		// inner classes

		private class RmultCoset implements Coset<Double> {

			private final double r;
			private final double logRX;

			RmultCoset(double r) {
				this.r = r;
				this.logRX = Math.log(r * x);
			}

			@Override
			public boolean contains(Double e) {
				return Math.log(e) % logRX == 0.0;
			}

			@Override
			public Equivalence<Double> equality() {
				return Equivalence.equality();
			}

			@Override
			public Size getSize() {
				return Size.COUNTABLY_INFINITE;
			}

			@Override
			public Side getSide() {
				return Side.BOTH;
			}

			@Override
			public Double getRepresentative() {
				return r;
			}

		}

	}

}
