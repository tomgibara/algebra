package com.tomgibara.algebra.group;

import java.math.BigInteger;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Random;
import java.util.Set;

import com.tomgibara.algebra.Size;
import com.tomgibara.algebra.group.Group.Operation;

import junit.framework.TestCase;

public class ZnTest extends TestCase {

	public void testIterator() {
		for (int n = 1; n < 20; n++) {
			Group<BigInteger> zn = Groups.Zn(BigInteger.valueOf(n));
			Set<BigInteger> set = new HashSet<BigInteger>();
			for (BigInteger j : zn) {
				if (!set.add(j)) fail();
			}
			for (int j = 0; j < n; j++) {
				assertTrue(set.contains(BigInteger.valueOf(j)));
			}
			assertEquals(set, zn.elements());
		}
	}

	public void testOp() {
		Random r = new Random(0L);
		for (int n = 1; n < 20; n++) {
			Group<BigInteger> zn = Groups.Zn(BigInteger.valueOf(n));
			Operation<BigInteger> op = zn.op();
			assertEquals(0, zn.op().identity().intValue());
			for (int j = 0; j < 100; j++) {
				BigInteger a = BigInteger.valueOf(r.nextInt(n));
				BigInteger b = BigInteger.valueOf(r.nextInt(n));
				assertEquals(a.add(b).remainder(zn.getSize().asBigInt()), op.compose(a, b));
			}
		}
	}

	public void testAbelian() {
		for (int n = 1; n < 20; n++) {
			Group<BigInteger> zn = Groups.Zn(BigInteger.valueOf(n));
			assertTrue(zn.isAbelian());
		}
	}
	
	public void testSize() {
		for (int n = 1; n < 20; n++) {
			Group<BigInteger> zn = Groups.Zn(BigInteger.valueOf(n));
			assertEquals(Size.fromInt(n), zn.getSize());
		}
	}

	public void testSubgroup() {
		Group<BigInteger> z12 = Groups.Zn(BigInteger.valueOf(12));
		BigInteger THREE = BigInteger.valueOf(3);
		Subgroup<BigInteger> s = z12.inducedSubgroup(THREE);
		Group<BigInteger> s3 = s.getSubgroup();
		assertEquals(4, s3.getSize().asInt());
		assertTrue(s3.contains(BigInteger.ZERO));
		assertFalse(s3.contains(BigInteger.ONE));
		assertTrue(s3.contains(THREE));
		assertEquals(4, s.leftCoset(BigInteger.ONE).getSize().asInt());
	}
}
