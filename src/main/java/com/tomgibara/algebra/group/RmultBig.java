package com.tomgibara.algebra.group;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Iterator;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.Equivalence;

import ch.obermuhlner.math.big.BigDecimalMath;

class RmultBig extends Rmult<BigDecimal> {

	private final MathContext context;
	private final int compareTo;

	private final Operation<BigDecimal> op = new Operation<BigDecimal>() {

		@Override public boolean isCommutative() { return true; }

		@Override public BigDecimal identity()                                   { return BigDecimal.ONE                   ; }
		@Override public BigDecimal compose(BigDecimal e1, BigDecimal e2)        { return e1.multiply(e2, context)         ; }
		@Override public BigDecimal composeInverse(BigDecimal e1, BigDecimal e2) { return e1.divide(e2, context)           ; }
		@Override public BigDecimal invert(BigDecimal e)                         { return BigDecimal.ONE.divide(e, context); }
		@Override public BigDecimal power(BigDecimal e, long p)                  { return e.pow((int) p, context)          ; } //TODO guard against too large p
		@Override public BigDecimal power(BigDecimal e, BigInteger p)            { return e.pow(p.intValue(), context)     ; }

	};

	RmultBig(MathContext context, int compareTo) {
		this.context = context;
		this.compareTo = compareTo;
	}

	@Override
	public Operation<BigDecimal> op() {
		return op;
	}

	@Override
	public boolean isIdentity(BigDecimal e) {
		if (e == null) throw new IllegalArgumentException("null e");
		return round(e).compareTo(BigDecimal.ONE) == 0;
	}

	@Override
	public boolean contains(BigDecimal e) {
		if (e == null) throw new IllegalArgumentException("null e");
		return round(e).signum() != 0;
	}

	@Override
	public Subgroup<BigDecimal> generatedSubgroup(BigDecimal... es) {
		if (es == null) throw new IllegalArgumentException("null es");
		switch (es.length) {
		case 0 : return Subgroup.trivialSubgroup(this);
		case 1: return new Single(es[0]);
		default: throw new UnsupportedOperationException("unsupported");
		}
	}

	private BigDecimal round(BigDecimal value) {
		return value.setScale(compareTo, RoundingMode.HALF_UP);
	}

	private boolean dividesBy(BigDecimal value, BigDecimal divisor, BigDecimal roundDivisor) {
		BigDecimal remainder = round( value.remainder(divisor) );
		return remainder.signum() == 0 || remainder.compareTo(roundDivisor) == 0;
	}

	private final class Single implements Subgroup<BigDecimal>, Group<BigDecimal> {

		private final BigDecimal x;
		private final BigDecimal logX;
		private final BigDecimal roundLogX;

		Single(BigDecimal x) {
			this.x = x;
			logX = BigDecimalMath.log(x, context);
			roundLogX = round(logX);
		}

		// subgroup methods

		@Override
		public Group<BigDecimal> getSubgroup() {
			return this;
		}

		@Override
		public Group<BigDecimal> getOvergroup() {
			return RmultBig.this;
		}

		@Override
		public Coset<BigDecimal> leftCoset(BigDecimal e) {
			if (e == null) throw new IllegalArgumentException("null e");
			return new RmultCoset(e);
		}

		@Override
		public Coset<BigDecimal> rightCoset(BigDecimal e) {
			if (e == null) throw new IllegalArgumentException("null e");
			return new RmultCoset(e);
		}

		// group methods

		@Override
		public Size getSize() {
			return Size.COUNTABLY_INFINITE;
		}

		@Override
		public boolean contains(BigDecimal e) {
			return dividesBy(BigDecimalMath.log(e, context), logX, roundLogX);
		}

		@Override
		public Equivalence<BigDecimal> equality() {
			return Equivalence.equality();
		}

		@Override
		public Iterator<BigDecimal> iterator() {
			return Groups.alternator(x, op);
		}

		@Override
		public Operation<BigDecimal> op() {
			return op;
		}

		// inner classes

		private class RmultCoset implements Coset<BigDecimal> {

			private final BigDecimal r;
			private final BigDecimal logRX;
			private final BigDecimal roundLogRX;

			RmultCoset(BigDecimal r) {
				this.r = r;
				logRX = BigDecimalMath.log(r.multiply(x, context), context);
				roundLogRX = round(logRX);
			}

			@Override
			public boolean contains(BigDecimal e) {
				return dividesBy(BigDecimalMath.log(e, context), logRX, roundLogRX);
			}

			@Override
			public Equivalence<BigDecimal> equality() {
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
			public BigDecimal getRepresentative() {
				return r;
			}

		}

	}

}
