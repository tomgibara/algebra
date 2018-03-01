package com.tomgibara.algebra.group;

import java.math.BigInteger;

// reals implemented with doubles
public class Rdbl extends R<Double> {

	private static final Operation<Double> op = new Operation<Double>() {

		@Override public boolean isCommutative() { return true; }

		@Override public Double identity()                           { return 0.0;                          }
		@Override public Double compose(Double e1, Double e2)        { return e1 + e2;                      }
		@Override public Double composeInverse(Double e1, Double e2) { return e1 - e2;                      }
		@Override public Double invert(Double e)                     { return -e;                           }
		@Override public Double power(Double e, long p)              { return e * p;                        }
		@Override public Double power(Double e, BigInteger p)        { return e * p.doubleValue();          }

	};

	static final Rdbl instance = new Rdbl();

	private Rdbl() { }

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
	Subgroup<Double> singleSubgroup(Double e) {
		if (e == null) throw new IllegalArgumentException("null e");
		return new Single(e);
	}

	private class Single extends R<Double>.RSubgroup {

		private final double x;

		Single(double x) {
			this.x = x;
		}

		// subgroup methods

		@Override
		Double x() {
			return x;
		}

		R<Double>.RSubgroup.RCoset newCoset(Double r) {
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
			return x % e == 0.0;
		}

		@Override
		public Subgroup<Double> generatedSubgroup(Double... es) {
		}

	}
}
