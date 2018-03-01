package com.tomgibara.algebra.group;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.Iterator;
import java.util.Set;

import org.junit.Assert;
import org.junit.Test;

import com.tomgibara.collect.Collect;

public class RdblTest {

	@Test
	public void testBasic() {
		Group<Double> r = Groups.Rdbl();
		assertTrue(r.contains(1.0));
		assertTrue(r.isIdentity(0.0));
		Subgroup<Double> z = r.generatedSubgroup(1.0);
		Iterator<Double> i = z.getSubgroup().iterator();
		Set<Double> dbls = Collect.setsOf(double.class).newSet();
		for (int j = 0; j < 1000; j++) {
			if (!dbls.add(i.next())) fail();
		}
		Subgroup<Double> rtoo = r.generatedSubgroup(1.0, Math.PI);
		assertEquals(r, rtoo.getSubgroup());
		assertEquals(7.0, r.op().compose(4.0, 3.0).doubleValue(), 0.0);
	}

	@Test
	public void testGeneratedSubgroup() {
		Group<Double> r = Groups.Rdbl();
		Group<Double> s = r.generatedSubgroup(2.0).getSubgroup();
		Group<Double> t = s.generatedSubgroup(4.0).getSubgroup();
		assertTrue(t.contains(4.0));
		assertFalse(t.contains(2.0));
	}
}
