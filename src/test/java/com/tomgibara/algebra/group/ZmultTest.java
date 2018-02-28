package com.tomgibara.algebra.group;

import static java.math.BigInteger.ONE;
import static java.math.BigInteger.ZERO;
import static java.math.BigInteger.valueOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.math.BigInteger;

import org.junit.Assert;
import org.junit.Test;

import com.tomgibara.algebra.Size;

public class ZmultTest {

	@Test
	public void testOrder() {
		Group<BigInteger> zm = Groups.Zmult(valueOf(5), 3);
		assertEquals(Size.fromInt(1), zm.orderOf(ONE));
		assertEquals(Size.fromInt(100), zm.orderOf(valueOf(2)));
		assertEquals(Size.fromInt(100), zm.orderOf(valueOf(3)));
		assertEquals(Size.fromInt(50), zm.orderOf(valueOf(4)));
	}

	@Test
	public void testContains() {
		Group<BigInteger> zm = Groups.Zmult(valueOf(5), 3);
		assertFalse(zm.contains(ZERO));
		assertFalse(zm.contains(valueOf(5)));
		assertFalse(zm.contains(valueOf(-1)));
		assertTrue(zm.contains(valueOf(1)));
		assertTrue(zm.contains(valueOf(124)));
	}
}
