package com.tomgibara.algebra;

import static com.tomgibara.algebra.Constants.MAX_LONG_VALUE;

import java.math.BigInteger;
import java.util.Arrays;

public final class Size {

	// private statics

	// -1 - finite (unknown)
	private static final int F = -1;
	// -2 - countable
	private static final int C = -2;
	// -3 - uncountable
	private static final int U = -3;

	private static Size fromConstant(int min) {
		switch (min) {
		case F : return FINITE;
		case C : return COUNTABLY_INFINITE;
		case U : return UNCOUNTABLY_INFINITE;
		default: throw new IllegalStateException();
		}
	}

	// public statics

	public static final Size FINITE               = new Size(F);
	public static final Size COUNTABLY_INFINITE   = new Size(C);
	public static final Size UNCOUNTABLY_INFINITE = new Size(U);
	public static final Size ONE                  = new Size(1L);

	public static Size fromInt(int size) {
		if (size < 1) throw new IllegalArgumentException("non-positive size");
		return new Size((long) size);
	}

	public static Size fromLong(long size) {
		if (size < 1L) throw new IllegalArgumentException("non-positive size");
		return new Size(size);
	}

	public static Size fromBig(BigInteger size) {
		if (size == null) throw new IllegalArgumentException("null size");
		if (size.signum() < 0) throw new IllegalArgumentException("non-positive size");
		return new Size(size);
	}

	public static Size product(Size... sizes) {
		if (sizes == null) throw new IllegalArgumentException("null sizes");
		switch (sizes.length) {
		case 0 : return ONE;
		case 1 : return sizes[0];
		case 2 : return sizes[0].product(sizes[1]);
		default:
			long min = Arrays.stream(sizes).mapToLong(size -> size.small).min().getAsLong();
			if (min < 0) return fromConstant((int) min);
			BigInteger big = Arrays.stream(sizes).map(size -> size.asBig()).reduce((a,b) -> a.multiply(b)).get();
			return new Size(big);
		}
	}

	private final long small;
	private BigInteger big;

	private Size(int type) {
		small = type;
		big = null;
	}

	private Size(long size) {
		small = size;
		big = null;
	}

	private Size(BigInteger size) {
		small = size.compareTo(MAX_LONG_VALUE) > 0 ? 0L : size.longValue();
		big = size;
	}

	public int asInt() {
		if (small < 1 || small > Integer.MAX_VALUE) throw new IllegalStateException("size too large");
		return (int) small;
	}

	public long asLong() {
		if (small < 1) throw new IllegalStateException("size too large");
		return small;
	}

	public BigInteger asBig() {
		if (big == null) {
			if (small < 0L) return null; //TODO should throw exception instead?
			big = BigInteger.valueOf(small);
		}
		return big;
	}

	//TODO rename to isInt?
	public boolean isSmall() {
		return small > 0L && small < Integer.MAX_VALUE;
	}

	public boolean isBig() {
		return small == 0L;
	}

	public boolean isFinite() {
		return small >= F;
	}

	public boolean isCountable() {
		return small >= C;
	}

	public boolean isInfinite() {
		return small < F;
	}

	public boolean isCountablyInfinite() {
		return small == C;
	}

	public boolean isUncountablyInfinite() {
		return small == U;
	}

	public Size product(Size multiplier) {
		long min = Math.min(this.small, multiplier.small);
		if (min < 0L) return fromConstant((int) min);
		return new Size(this.asBig().multiply(multiplier.asBig()));
	}

	public Size quotient(Size divisor) {
		if (divisor.small <= 0 && divisor.small < this.small) throw new IllegalArgumentException("too large");

		// unknown / infinite case
		if (this.small < 0) {
			return this;
		}

		// finite size
		if (this.small == 0) { // big case
			BigInteger[] qr = big.divideAndRemainder(divisor.asBig());
			if (qr[1].signum() != 0) throw new IllegalArgumentException("not divisor");
			return new Size(qr[0]);
		}

		// non-big case
		long q = this.small / divisor.small;
		long r = this.small - (divisor.small * q);
		if (r != 0L) throw new IllegalArgumentException("not divisor");
		return new Size(q);
	}

	@Override
	public int hashCode() {
		if (small != 0) return Long.hashCode(small);
		return big.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof Size)) return false;
		Size that = (Size) obj;
		if (this.small != that.small) return false;
		return this.small != 0 || this.big.equals(that.big);
	}

	@Override
	public String toString() {
		if (small == U) return "uncountably infinite";
		if (small == C) return "countably infinite";
		if (small == F) return "finite";
		if (small == 0) return big.toString();
		return Long.toString(small);
	}

}