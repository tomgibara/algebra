package com.tomgibara.algebra.group;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import com.tomgibara.collect.Collect;

public class RaddBigTest {

	@Test
	public void testBasic() {
		Group<BigDecimal> r = Groups.RaddBig();
		assertTrue(r.contains(ONE));
		assertTrue(r.isIdentity(ZERO));
		assertEquals(1, r.generatedSubgroup(ZERO).getSubgroup().getSize().asInt());
		Group<BigDecimal> s = r.generatedSubgroup(new BigDecimal("0.2"), new BigDecimal("0.25")).getSubgroup();
		assertTrue(s.contains(new BigDecimal("0.05")));
		assertTrue(s.contains(new BigDecimal("0.050")));
		Iterator<BigDecimal> i = s.iterator();
		Set<BigDecimal> bigs = Collect.setsOf(BigDecimal.class).newSet();
		for (int j = 0; j < 1000; j++) {
			if (!bigs.add(i.next())) fail();
		}
	}

	@Test
	public void testGeneratedSubgroup() {
		Group<BigDecimal> r = Groups.RaddBig();
		BigDecimal TWO = new BigDecimal("2.0");
		BigDecimal FOUR = new BigDecimal("4.0");
		Group<BigDecimal> s = r.generatedSubgroup(TWO).getSubgroup();
		Group<BigDecimal> t = s.generatedSubgroup(FOUR).getSubgroup();
		assertTrue(t.contains(FOUR));
		assertFalse(t.contains(TWO));
	}

	@Test
	public void testEquality() {
		assertTrue(Groups.RaddBig().equality().isEquivalent(new BigDecimal("2.0"), new BigDecimal("2")));
	}
}
