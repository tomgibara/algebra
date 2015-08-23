package com.tomgibara.algebra.group;

import java.math.BigInteger;

import com.tomgibara.algebra.Size;
import com.tomgibara.algebra.group.Coset.Side;
import com.tomgibara.collect.EquRel;

public class ZnCoset extends AbstractCoset<BigInteger> {

	private final Zn zn;
	private final BigInteger r;
	
	ZnCoset(Zn zn, BigInteger r) {
		this.zn = zn;
		this.r = r;
	}
	
	@Override
	public Size getSize() {
		return Size.fromBig(zn.g);
	}

	@Override
	public boolean contains(BigInteger e) {
		return zn.cosetEqu.isEquivalent(r, e);
	}
	
	@Override
	public EquRel<BigInteger> equality() {
		return zn.cosetEqu;
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
		if (!(obj instanceof ZnCoset)) return false;
		ZnCoset that = (ZnCoset) obj;
		if (!this.zn.equals(that.zn)) return false;
		if (!zn.cosetEqu.isEquivalent(this.r, that.r)) return false;
		return true;
	}

}
