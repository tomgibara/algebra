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

import com.tomgibara.algebra.poset.PartialOrder;
import com.tomgibara.collect.EquRel;

//TODO can optimize implementation of bounded methods by checking for equality with top/bottom
// no method on this interface can take nulls
public interface Lattice<E> extends MeetSemiLattice<E>, JoinSemiLattice<E>, PartialOrder<E> {

	// req. lattice top v supplied top = lattice top
	// returns a new lattice bounded above by supplied top
	@Override
	Lattice<E> boundedAbove(E top);

	// req  lattice bottom ^ supplied bottom = lattice bottom
	// returns a new lattice bounded below by supplied top
	@Override
	Lattice<E> boundedBelow(E bottom);

	// req. lattice top v supplied top = lattice top
	// req  lattice bottom ^ supplied bottom = lattice bottom
	// returns a new lattice bounded above and below by supplied top and bottom
	default Lattice<E> bounded(E top, E bottom) {
		return boundedAbove(top).boundedBelow(bottom);
	}

	// returns true if bounded above and below
	default boolean isBounded() {
		return isBoundedBelow() && isBoundedAbove();
	}

	@Override
	default EquRel<E> equality() {
		return (e1, e2) -> {
			if (!contains(e1) || !contains(e2)) throw new IllegalArgumentException();
			E m = meet(e1, e2);
			E j = join(e1, e2);
			return m.equals(j);
		};
	}
	
	@Override
	default boolean isOrdered(E e1, E e2) {
		if (!contains(e1) || !contains(e2)) throw new IllegalArgumentException();
		return join(e1, e2).equals(e2);
	}
}
