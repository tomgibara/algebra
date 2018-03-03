package com.tomgibara.algebra.group;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static java.math.BigDecimal.valueOf;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import com.tomgibara.collect.Collect;
import com.tomgibara.collect.EquivalenceSet;

public class RmultBigTest {

	@Test
	public void testBasic() {
		MathContext context = new MathContext(50);
		BigDecimal TWO   = valueOf(2);
		BigDecimal THREE = valueOf(3);
		BigDecimal FOUR  = valueOf(4);
		Group<BigDecimal> g = Groups.RmultBig(context, 45);
		assertEquals(valueOf(9), g.op().compose(THREE, THREE));
		assertTrue(g.contains(ONE));
		assertFalse(g.contains(ZERO));
		Group<BigDecimal> h = g.generatedSubgroup(TWO).getSubgroup();
		assertTrue(h.contains(TWO));
		assertFalse(h.contains(THREE));
		assertTrue(h.contains(FOUR));
		Iterator<BigDecimal> i = h.iterator();
		EquivalenceSet<BigDecimal> set = Collect.<BigDecimal>sets().newSet();
		for (int j = 0; j < 100; j++) {
			if (!set.add(i.next())) fail();
		}
	}
}
