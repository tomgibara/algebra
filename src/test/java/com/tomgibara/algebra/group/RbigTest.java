package com.tomgibara.algebra.group;

import static java.math.BigDecimal.ONE;
import static java.math.BigDecimal.ZERO;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.math.BigDecimal;
import java.util.Iterator;
import java.util.Set;

import org.junit.Test;

import com.tomgibara.collect.Collect;

public class RbigTest {

	@Test
	public void testBasic() {
		Group<BigDecimal> r = Groups.Rbig();
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
}
