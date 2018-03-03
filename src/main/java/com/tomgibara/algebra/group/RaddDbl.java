package com.tomgibara.algebra.group;

import java.math.BigInteger;

// reals implemented with doubles
public class RaddDbl extends Radd<Double> {

	private static final Operation<Double> op = new Operation<Double>() {

		@Override public boolean isCommutative() { return true; }

		@Override public Double identity()                           { return 0.0;                          }
		@Override public Double compose(Double e1, Double e2)        { return e1 + e2;                      }
		@Override public Double composeInverse(Double e1, Double e2) { return e1 - e2;                      }
		@Override public Double invert(Double e)                     { return -e;                           }
		@Override public Double power(Double e, long p)              { return e * p;                        }
		@Override public Double power(Double e, BigInteger p)        { return e * p.doubleValue();          }

	};

	static final RaddDbl instance = new RaddDbl();

	private RaddDbl() { }

	@Override
	public boolean isIdentity(Double e) {
		if (e == null) throw new IllegalArgumentException("null e");
		return e == 0f;
	}

	@Override
	public boolean contains(Double e) {
		if (e == null) throw new IllegalArgumentException("null e");
		return !e.isNaN();
	}

	@Override
	public Operation<Double> op() {
		return op;
	}

	@Override
	public Subgroup<Double> generatedSubgroup(Double... es) {
		if (es == null) throw new IllegalArgumentException("null es");
		switch (es.length) {
		case 1 :
			Double e = es[0];
			if (e == null) throw new IllegalArgumentException("null e");
			if (e != 0.0) return new Single(e);
			// fall through
		case 0 :
			return Subgroup.trivialSubgroup(this);
		default:
			// in general we can't establish gcd
			return Subgroup.totalSubgroup(this);
		}
	}

	private class Single extends Radd<Double>.RSubgroup {

		private final double x;

		Single(double x) {
			this.x = x;
		}

		// subgroup methods

		@Override
		Double x() {
			return x;
		}

		Radd<Double>.RSubgroup.RCoset newCoset(Double r) {
			if (r == null) throw new IllegalArgumentException("null r");
			return new RCoset() {

				public Double getRepresentative() {
					return r;
				}

				@Override
				public boolean contains(Double e) {
					if (e == null) throw new IllegalArgumentException("null e");
					//TODO should apply delta?
					return (e - r) % x == 0f;
				}

			};
		}

		// group methods

		@Override
		public boolean contains(Double e) {
			if (e == null) throw new IllegalArgumentException("null e");
			return e % x == 0.0;
		}

		@Override
		public Subgroup<Double> generatedSubgroup(Double... es) {
			// check es are valid
			if (es == null) throw new IllegalArgumentException("null es");
			for (Double e : es) {
				if (!contains(e)) throw new IllegalArgumentException("invalid e");
			}
			switch (es.length) {
			case 0 :
				return Subgroup.trivialSubgroup(this);
			case 1 :
				double e = es[0];
				if (e == x  ) return Subgroup.totalSubgroup(this);
				if (e == 0.0) return Subgroup.trivialSubgroup(this);
				return new Single(x);
			default:
				return Subgroup.totalSubgroup(this);
			}
		}

	}
}
