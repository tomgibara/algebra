package com.tomgibara.algebra.group;

import java.math.BigInteger;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.EquRel;

class ZCoset extends AbstractCoset<BigInteger> {

	private final Z z;
	private final BigInteger r;
	
	ZCoset(Z z, BigInteger r) {
		this.z = z;
		this.r = r.remainder(z.g);
	}
	
	@Override
	public Size getSize() {
		return Size.COUNTABLY_INFINITE;
	}
	
	@Override
	public boolean contains(BigInteger e) {
		return z.cosetEqu.isEquivalent(r, e);
	}

	@Override
	public EquRel<BigInteger> equality() {
		return z.cosetEqu;
	}

	@Override
	public Side getSide() {
		return Side.BOTH;
	}

	@Override
	public BigInteger getRepresentative() {
		return r;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == this) return true;
		if (!(obj instanceof ZCoset)) return false;
		ZCoset that = (ZCoset) obj;
		if (!this.z.equals(that.z)) return false;
		if (!z.cosetEqu.isEquivalent(this.r, that.r)) return false;
		return true;
	}
	
	
}