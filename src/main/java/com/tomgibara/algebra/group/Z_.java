package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.Arrays;

abstract class Z_ implements Group<BigInteger> {

	static final BigInteger gcd(BigInteger... es) {
		return Arrays.stream(es).filter(e -> e.signum() != 0).map(e -> e.abs()).distinct().reduce((a,b) -> a.gcd(b)).orElse(BigInteger.ZERO);
	}

}
