package com.tomgibara.algebra.group;

import java.util.HashSet;
import java.util.Set;

import junit.framework.TestCase;

import com.tomgibara.algebra.Size;
import com.tomgibara.permute.Permutation;

public class STest extends TestCase {

	public void testAbelian() {
		for (int i = 0; i < 6; i++) {
			assertEquals(i < 3, Groups.S(i).isAbelian());
		}
	}
	
	public void testIterator() {
		for (int i = 0; i < 6; i++) {
			Set<Permutation> set = new HashSet<>();
			Group<Permutation> s = Groups.S(i);
			for (Permutation p : s) { set.add(p); }
			assertEquals(s.getSize().asInt(), set.size());
		}
	}
	
	public void testIdentity() {
		for (int i = 0; i < 6; i++) {
			assertEquals(Permutation.identity(i), Groups.S(i).op().identity());
		}
	}

	public void testSize() {
		assertEquals(Size.fromInt(1), Groups.S(0).getSize());
		assertEquals(Size.fromInt(1), Groups.S(1).getSize());
		assertEquals(Size.fromInt(24), Groups.S(4).getSize());
	}
	
	public void testInduced() {
		Groups.S(4).generatedSubgroup(Permutation.rotate(4, 1));
	}
	
	public void testSubgroup() {
		Group<Permutation> s4 = Groups.S(4);
		Subgroup<Permutation> sub = s4.generatedSubgroup(Permutation.rotate(4, 1));
		Group<Permutation> c3 = sub.getSubgroup();
		assertEquals(3, c3.getSize().asInt());
		Coset<Permutation> coset = sub.leftCoset(Permutation.transpose(4, 0, 1));
	}

}
