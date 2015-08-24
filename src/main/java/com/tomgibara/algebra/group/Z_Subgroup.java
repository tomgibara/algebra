package com.tomgibara.algebra.group;

import java.math.BigInteger;

class Z_Subgroup implements Subgroup<BigInteger> {

	private final Z_ overgroup;
	private final Z_ subgroup;

	Z_Subgroup(Z_ overgroup, BigInteger g) {
		this.overgroup = overgroup;
		subgroup = overgroup.subgroup(g);
	}
	
	@Override
	public Group<BigInteger> getOvergroup() {
		return overgroup;
	}
	
	@Override
	public Group<BigInteger> getSubgroup() {
		return subgroup;
	}

	@Override
	public Coset<BigInteger> rightCoset(BigInteger e) {
		return new Z_Coset(subgroup, e);
	}

	@Override
	public Coset<BigInteger> leftCoset(BigInteger e) {
		return new Z_Coset(subgroup, e);
	}

}
