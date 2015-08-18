package com.tomgibara.algebra;

import static com.tomgibara.algebra.Constants.MAX_LONG_VALUE;

import java.math.BigInteger;
import java.util.Arrays;

//TODO construct via static accessors
public final class Order {
	
	// private statics
	
	// -1 - finite (unknown)
	private static final int F = -1;
	// -2 - countable
	private static final int C = -2;
	// -3 - uncountable
	private static final int U = -3;
	
	private static Order fromConstant(int min) {
		switch (min) {
		case F : return FINITE;
		case C : return COUNTABLY_INFINITE;
		case U : return UNCOUNTABLY_INFINITE;
		default: throw new IllegalStateException();
		}
	}
	
	// public statics
	
	public static final Order FINITE               = new Order(F);
	public static final Order COUNTABLY_INFINITE   = new Order(C);
	public static final Order UNCOUNTABLY_INFINITE = new Order(U);
	public static final Order ONE                  = new Order(1L);
	
	public static Order fromLong(long order) {
		if (order < 1L) throw new IllegalArgumentException("non-positive order");
		return new Order(order);
	}
	
	public static Order fromBig(BigInteger order) {
		if (order == null) throw new IllegalArgumentException("null order");
		if (order.signum() < 0) throw new IllegalArgumentException("non-positive order");
		return new Order(order);
	}
	
	public static Order product(Order... orders) {
		if (orders == null) throw new IllegalArgumentException("null orders");
		switch (orders.length) {
		case 0 : return ONE;
		case 1 : return orders[0];
		case 2 : return orders[0].product(orders[1]);
		default:
			long min = Arrays.stream(orders).mapToLong(order -> order.small).min().getAsLong();
			if (min < 0) return fromConstant((int) min);
			BigInteger big = Arrays.stream(orders).map(order -> order.asBigInt()).reduce((a,b) -> a.multiply(b)).get();
			return new Order(big);
		}
	}
	
	private final long small;
	private BigInteger big;
	
	private Order(int type) {
		small = type;
		big = null;
	}
	
	private Order(long order) {
		small = order;
		big = null;
	}
	
	private Order(BigInteger order) {
		small = order.compareTo(MAX_LONG_VALUE) > 0 ? 0L : order.longValue();
		big = order;
	}
	
	public int asInt() {
		if (small < 1 || small > Integer.MAX_VALUE) throw new IllegalStateException("order too large");
		return (int) small;
	}
	
	public long asLong() {
		if (small < 1) throw new IllegalStateException("order too large");
		return small;
	}
	
	public BigInteger asBigInt() {
		if (big == null) {
			if (small < 0L) return null; //TODO should throw exception instead?
			big = BigInteger.valueOf(small);
		}
		return big;
	}
	
	public boolean isSmall() {
		return small > 0L && small < Integer.MAX_VALUE;
	}
	
	public boolean isBig() {
		return small == 0L;
	}
	
	public boolean isFinite() {
		return small >= 0L;
	}
	
	public boolean isCountable() {
		return small >= -1L;
	}

	public boolean isInfinite() {
		return small < 0;
	}

	public boolean isCountablyInfinite() {
		return small == -1L;
	}
	
	public boolean isUncountablyInfinite() {
		return small == -2L;
	}
	
	public Order product(Order that) {
		long min = Math.min(this.small, that.small);
		if (min < 0L) return fromConstant((int) min);
		return new Order(this.asBigInt().multiply(that.asBigInt()));
	}
	
	@Override
	public int hashCode() {
		if (small != 0) return Long.hashCode(small);
		return big.hashCode();
	}

	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof Order)) return false;
		Order that = (Order) obj;
		if (this.small != that.small) return false;
		return this.small != 0 || this.big.equals(that.big);
	}
	
	@Override
	public String toString() {
		if (small == -2) return "uncountably infinite";
		if (small == -1) return "countably infinite";
		if (small == 0) return big.toString();
		return Long.toString(small);
	}
	
}