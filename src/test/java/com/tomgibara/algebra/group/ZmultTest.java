package com.tomgibara.algebra.group;

import static org.junit.Assert.assertEquals;

import java.math.BigInteger;

import org.junit.Test;

import com.tomgibara.algebra.Size;

public class ZmultTest {

	@Test
	public void testOrder() {
		Group<BigInteger> zm = Groups.Zmult(BigInteger.valueOf(5), 3);
		assertEquals(Size.fromInt(1), zm.orderOf(BigInteger.ONE));
		assertEquals(Size.fromInt(124), zm.orderOf(BigInteger.valueOf(2)));
		assertEquals(Size.fromInt(24), zm.orderOf(BigInteger.valueOf(5)));
	}
	
}
