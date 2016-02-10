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

import java.util.function.Function;
import java.util.function.UnaryOperator;

public interface MeetSemiLattice<E> extends SemiLattice<E> {

	//if unbounded, must accept null arguments (representing bottom)
	E meet(E a, E b);

	E getBottom();

	MeetSemiLattice<E> boundedBelow(E bottom);

	default boolean isBoundedBelow() {
		return getBottom() == null;
	}

	default UnaryOperator<E> endomorphism(MeetSemiLattice<E> subLattice) {
		return x -> meet(x, subLattice.getBottom());
	}
}
