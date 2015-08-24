package com.tomgibara.algebra.group;

import java.math.BigInteger;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.EquRel;

class Z_Coset extends AbstractCoset<BigInteger> {
	
	private final Z_ z_;
	private final BigInteger r;
	
	Z_Coset(Z_ z_, BigInteger r) {
		this.z_ = z_;
		this.r = r.remainder(z_.g);
	}
	
	@Override
	public Size getSize() {
		return z_.getSize();
	}

	@Override
	public boolean contains(BigInteger e) {
		return z_.cosetEqu.isEquivalent(r, e);
	}
	
	@Override
	public EquRel<BigInteger> equality() {
		return z_.cosetEqu;
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
		if (!(obj instanceof Z_Coset)) return false;
		Z_Coset that = (Z_Coset) obj;
		if (!this.z_.equals(that.z_)) return false;
		if (!z_.cosetEqu.isEquivalent(this.r, that.r)) return false;
		return true;
	}

}
