package com.tomgibara.algebra;

import static com.tomgibara.algebra.Constants.MAX_LONG_VALUE;

import java.math.BigInteger;

//TODO construct via static accessors
public final class Order {
	
	public static final Order COUNTABLY_INFINITE = new Order(true);
	public static final Order UNCOUNTABLY_INFINITE = new Order(false);
	
	//  0 - big
	// -1 - countable
	// -2 - uncountable
	private final long small;
	private BigInteger big;
	
	private Order(boolean countable) {
		small = countable ? -1 : -2;
		big = null;
	}
	
	public Order(long order) {
		if (order < 1L) throw new IllegalArgumentException("non-positive order");
		small = order;
		big = null;
	}
	
	public Order(BigInteger order) {
		if (order == null) throw new IllegalArgumentException("null order");
		small = order.compareTo(MAX_LONG_VALUE) > 0 ? 0 : order.longValue();
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
			if (small < 0) return null; //TODO should throw exception instead?
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