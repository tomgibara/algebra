package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.Arrays;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.EquRel;

abstract class Z_ implements Group<BigInteger> {

	// statics
	static final BigInteger gcd(BigInteger... es) {
		return Arrays.stream(es).filter(e -> e.signum() != 0).map(e -> e.abs()).distinct().reduce((a,b) -> a.gcd(b)).orElse(BigInteger.ZERO);
	}

	// fields

	final BigInteger g; // divides n
	final EquRel<BigInteger> cosetEqu;

	// constructors

	public Z_(BigInteger g, EquRel<BigInteger> cosetEqu) {
		this.g = g.equals(BigInteger.ONE) ? BigInteger.ONE : g;
		this.cosetEqu = cosetEqu;
	}

	// group methods

	@Override
	public EquRel<BigInteger> equality() {
		return EquRel.equality();
	}

	@Override
	public boolean commutes(BigInteger e1, BigInteger e2) {
		return true;
	}

	@Override
	public boolean isAbelian() {
		return true;
	}

	@Override
	public boolean isIdentity(BigInteger e) {
		return e.signum() == 0;
	}

	@Override
	public Size orderOf(BigInteger e) {
		//TODO should test containment?
		if (e.signum() == 0) return Size.ONE;
		Size size = getSize();
		if (!size.isFinite()) return size;
		BigInteger n = size.asBig();
		return Size.fromBig( n.divide( e.gcd(n) ) );
	}

	// package scoped methods

	boolean containsImpl(BigInteger e) {
		return g == BigInteger.ONE || e.remainder(g).signum() == 0;
	}

	Subgroup<BigInteger> generated(BigInteger... es) {
		BigInteger gcd = gcd(es);
		if (gcd.signum() == 0) return Subgroup.trivialSubgroup(this);
		if (gcd.equals(g)) return Subgroup.totalSubgroup(this);
		if (gcd.remainder(g).signum() != 0) throw new IllegalArgumentException("non-element");
		return new Z_Subgroup(this, gcd);
	}

	abstract Z_ subgroup(BigInteger g);

}
