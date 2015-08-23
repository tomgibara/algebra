package com.tomgibara.algebra.group;

import java.math.BigInteger;

import com.tomgibara.collect.EquRel;
import com.tomgibara.hashing.HashCode;
import com.tomgibara.hashing.HashSize;
import com.tomgibara.hashing.Hasher;

class ZCosetEqu implements EquRel<BigInteger> {

	static EquRel<BigInteger> forGenerator(BigInteger g) {
		return g.equals(BigInteger.ONE) ? EquRel.equality() : new ZCosetEqu(g);
	}
	
	private final BigInteger g;
	
	private ZCosetEqu(BigInteger g) {
		this.g = g;
	}
	
	@Override
	public boolean isEquivalent(BigInteger e1, BigInteger e2) {
		return e1.subtract(e2).remainder(g).signum() == 0;
	}

	@Override
	public Hasher<BigInteger> getHasher() {
		return new Hasher<BigInteger>() {
			@Override
			public HashSize getSize() {
				return HashSize.INT_SIZE;
			}
			@Override
			public HashCode hash(BigInteger value) {
				return HashCode.fromInt(intHashValue(value));
			}
			@Override
			public int intHashValue(BigInteger value) throws IllegalArgumentException {
				if (value == null) throw new IllegalArgumentException("null value");
				return value.remainder(g).hashCode();
			}
		};
	}

}
