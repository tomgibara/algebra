package com.tomgibara.algebra.group;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Assert;
import org.junit.Test;

import com.tomgibara.algebra.Size;

public class TrivialTest {

	@Test
	public void testBasic() {
		Group<Integer> g = Groups.trivial(1);
		assertEquals(1, g.op().identity().intValue());
		assertEquals(1, g.op().compose(1, 1).intValue());
		assertEquals(Size.ONE, g.orderOf(1));
		assertTrue(g.contains(1));
		assertFalse(g.contains(2));
	}

}
