/*
 * Copyright 2010 Tom Gibara
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0

 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */
package com.tomgibara.algebra.lattice;

import java.math.BigInteger;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import com.tomgibara.algebra.Size;
import com.tomgibara.collect.Equivalence;

//TODO consider whether unbounded sets could be supported
public class SetLattice<E> implements Lattice<Set<E>> {

	private final Set<E> top;
	private final Set<E> bottom;
	private final Size size;

	public SetLattice(Set<E> top) {
		this(top, Collections.emptySet());
	}

	public SetLattice(Set<E> top, Set<E> bottom) {
		if (top == null) throw new IllegalArgumentException();
		if (bottom == null) throw new IllegalArgumentException();
		if (!top.containsAll(bottom)) throw new IllegalArgumentException();
		this.top = top;
		this.bottom = bottom;
		int size = top.size();
		this.size = size < 63 ? Size.fromLong(1L << size) : Size.fromBig(BigInteger.ONE.shiftLeft(size));
	}

	@Override
	public Size getSize() {
		return size;
	}

	@Override
	public Set<E> getTop() {
		return top;
	}

	@Override
	public Set<E> getBottom() {
		return bottom;
	}

	@Override
	public boolean isBoundedAbove() {
		return true;
	}

	@Override
	public boolean isBoundedBelow() {
		return true;
	}

	@Override
	public boolean isBounded() {
		return true;
	}

	@Override
	public boolean contains(Set<E> e) {
		return top.containsAll(e) && e.containsAll(bottom);
	}

	@Override
	public Set<E> join(Set<E> a, Set<E> b) {
		int as = a.size();
		int bs = b.size();
		Set<E> c;
		if (as > bs) {
			if (bs == 0) return a;
			c = mutableCopy(a);
			c.addAll(b);
		} else {
			if (as == 0) return b;
			c = mutableCopy(b);
			c.addAll(a);
		}
		return immutable(c);
	}

	@Override
	public Set<E> meet(Set<E> a, Set<E> b) {
		int as = a.size();
		int bs = b.size();
		Set<E> c;
		if (as > bs) {
			if (bs == 0) return b;
			c = mutableCopy(a);
			c.retainAll(b);
		} else {
			if (as == 0) return a;
			c = mutableCopy(b);
			c.retainAll(a);
		}
		return immutable(c);
	}

	@Override
	public Lattice<Set<E>> boundedAbove(Set<E> top) {
		if (!top.containsAll(bottom)) throw new IllegalArgumentException();
		return new SetLattice<E>(top, bottom);
	}

	@Override
	public Lattice<Set<E>> boundedBelow(Set<E> bottom) {
		if (!top.containsAll(bottom)) throw new IllegalArgumentException();
		return new SetLattice<E>(top, bottom);
	}

	@Override
	public Lattice<Set<E>> bounded(Set<E> top, Set<E> bottom) {
		if (!this.top.containsAll(top)) throw new IllegalArgumentException();
		if (!bottom.containsAll(this.bottom)) throw new IllegalArgumentException();
		return new SetLattice<E>(top, bottom);
	}

	@Override
	public Equivalence<Set<E>> equality() {
		return (a,b) -> {
			if (!contains(a) || !contains(b)) throw new IllegalArgumentException();
			return a.equals(b);
		};
	}

	@Override
	public boolean isOrdered(Set<E> a, Set<E> b) {
		if (!contains(a) && !(contains(b))) throw new IllegalArgumentException();
		return b.containsAll(a);
	}

	// methods for overriding

	protected Set<E> mutableCopy(Set<E> set) {
		return new HashSet<E>(set);
	}

	protected Set<E> immutable(Set<E> set) {
		return Collections.unmodifiableSet(set);
	}

	// object methods

	@Override
	public String toString() {
		return top + " to " + bottom;
	}

}
