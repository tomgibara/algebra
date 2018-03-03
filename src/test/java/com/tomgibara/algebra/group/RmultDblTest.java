package com.tomgibara.algebra.group;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;

import org.junit.Assert;
import org.junit.Test;

import com.tomgibara.collect.Collect;
import com.tomgibara.collect.EquivalenceSet;

public class RmultDblTest {

	@Test
	public void testBasic() {
		Group<Double> g = Groups.RmultDbl();
		assertEquals(9.0, g.op().compose(3.0, 3.0).doubleValue(), 0.0);
		assertTrue(g.contains(1.0));
		assertFalse(g.contains(0.0));
		Group<Double> h = g.generatedSubgroup(2.0).getSubgroup();
		assertTrue(h.contains(2.0));
		assertFalse(h.contains(3.0));
		assertTrue(h.contains(4.0));
		Iterator<Double> i = h.iterator();
		EquivalenceSet<Double> set = Collect.<Double>sets().newSet();
		for (int j = 0; j < 100; j++) {
			if (!set.add(i.next())) fail();
		}
	}
}
