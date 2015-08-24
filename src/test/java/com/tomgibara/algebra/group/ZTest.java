package com.tomgibara.algebra.group;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import junit.framework.TestCase;

public class ZTest extends TestCase {

	private Random random = new Random(0);
	
	private BigInteger randomBig() {
		int length = random.nextInt(8);
		if (length == 0) return ZERO;
		byte[] bytes = new byte[length];
		random.nextBytes(bytes);
		BigInteger big = new BigInteger(bytes);
		return big;
	}
	
	public void testBasic() {
		Group<BigInteger> z = Groups.Z();
		assertTrue(z.isAbelian());
		BigInteger identity = z.op().identity();
		assertEquals(ZERO, identity);
		for (int i = 0; i < 100; i++) {
			BigInteger a = randomBig();
			BigInteger b = randomBig();
			assertEquals(a.add(b), z.op().compose(a, b));
		}
		BigInteger five = BigInteger.valueOf(5);
		Subgroup<BigInteger> subgroup = z.inducedSubgroup(five);
		assertTrue(subgroup.getSubgroup().contains(ZERO));
		assertFalse(subgroup.getSubgroup().contains(ONE));
		assertTrue(subgroup.getSubgroup().contains(five));
		Coset<BigInteger> cs0 = subgroup.leftCoset(ZERO);
		assertEquals(ZERO, cs0.getRepresentative());
		Coset<BigInteger> cs5 = subgroup.leftCoset(five);
		assertEquals(ZERO, cs5.getRepresentative());
		BigInteger six = BigInteger.valueOf(6);
		Coset<BigInteger> cs1 = subgroup.leftCoset(six);
		assertEquals(ONE, cs1.getRepresentative());
		assertEquals(cs0, cs5);
		assertEquals(cs0.hashCode(), cs5.hashCode());
		assertTrue(cs1.contains(BigInteger.ONE));
		assertFalse(cs1.contains(five));
		assertTrue(cs1.equality().isEquivalent(BigInteger.ONE, six));

		Set<BigInteger> set = new HashSet<BigInteger>();
		Iterator<BigInteger> it = z.iterator();
		for (int i = 0; i < 100; i++) {
			if (!set.add(it.next())) fail( i + " iterant was duplicate");
		}
	}

}
